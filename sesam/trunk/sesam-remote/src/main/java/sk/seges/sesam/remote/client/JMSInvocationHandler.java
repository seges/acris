/**
 * 
 */
package sk.seges.sesam.remote.client;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.management.remote.JMXConnector;

import org.apache.log4j.Logger;

import sk.seges.sesam.remote.domain.InvocationContext;
import sk.seges.sesam.remote.domain.JMSBridgeConfiguration;
import sk.seges.sesam.remote.domain.RemoteCommand;
import sk.seges.sesam.remote.domain.Response;
import sk.seges.sesam.remote.exception.RemoteInvocationException;
import sk.seges.sesam.remote.jmx.IInvocationCollector;
import sk.seges.sesam.remote.jmx.domain.InvocationInfo;
import sk.seges.sesam.remote.jmx.domain.Side;
import sk.seges.sesam.remote.jmx.util.CollectorUtils;
import sk.seges.sesam.remote.util.JMSUtils;

/**
 * @author LGazo
 * @author AAlac
 */
public class JMSInvocationHandler<T> implements ClientProxyFactory<T>,
		InvocationHandler, ExceptionListener {
    private static final Logger log = Logger.getLogger(JMSInvocationHandler.class);
    
    private final JMSBridgeConfiguration configuration;
    private ExceptionListener listener;
    private IJMSExceptionAnalyzer analyzer;
    
    private static ThreadLocal<InvocationContext> threadInvocationContext = new ThreadLocal<InvocationContext>();
    private Thread currentThread = null;
    
	@Override
	public T createProxy(Class<T> invokedClass) {
		if(log.isDebugEnabled())
            log.debug("Creating JMS Invocation handler proxy for = " + invokedClass + ", with configuration = " + configuration);
		return invokedClass.cast(Proxy.newProxyInstance(invokedClass
					.getClassLoader(), new Class[] { invokedClass },
					new JMSInvocationHandler<T>(configuration)));
	}

	@SuppressWarnings("unchecked")
	@Override
	public T createProxy(String invokedClassName) {
		try {
			return createProxy((Class<T>) Class.forName(invokedClassName));
		} catch (ClassNotFoundException e) {
			throw new RemoteInvocationException("Cannot find class with name "
					+ invokedClassName, e);
		}
	}

	public T createProxy(Class<T> invokedClass, IJMSExceptionAnalyzer analyzer) {
        JMSInvocationHandler<T> handler = new JMSInvocationHandler<T>(configuration);
        handler.setAnalyzer(analyzer);
        analyzer.setConnectionFactory(configuration.getFactory());
        
		if(log.isDebugEnabled())
            log.debug("Creating JMS Invocation handler proxy for = " + invokedClass + ", with configuration = " + configuration);
        
        return invokedClass.cast(Proxy.newProxyInstance(invokedClass.getClassLoader(), new Class[] { invokedClass },
                handler));
    }
	
	public JMSInvocationHandler(JMSBridgeConfiguration configuration) {
        this.configuration = configuration;        
    }

	private void checkThreadConcurrency(RemoteCommand command) {
        Thread thread = Thread.currentThread();
        if(currentThread != null && !currentThread.equals(thread)) {
            if(log.isDebugEnabled())
                log.debug("Different thread (" + thread + ") is accessing handler owned by " + currentThread + ", trying to invoke " + command);
        } else if(currentThread == null) {
            currentThread = thread;
            if(log.isDebugEnabled())
                log.debug("Claimed invocation handler for thread = " + thread + ", command = " + command);
        }
    }
	
	/**
     * Prepare invocation context needed for current invocation. Invocation
     * context is local to current thread calling invoke method.
     * 
     * @throws JMSException
     */
    private InvocationContext prepare() throws JMSException {
        InvocationContext context = threadInvocationContext.get(); 
        if(context == null) {
            context = new InvocationContext();
            threadInvocationContext.set(context);
        }
        
        if(CollectorUtils.activateJMX(configuration)) {
            JMXConnector connector;
            try {
                connector = CollectorUtils.establishConnection(configuration.getJmxServiceURL());
                context.setJmxConnector(connector);
            } catch (IOException e) {
                log.warn("JMX connector in invocation handler deactivated because of exception with configuration = " + configuration, e);
                connector = null;
            }            
        }
        
        context.setTransportAsynchronouslyInterrupted(false);
        
        Connection connection = configuration.getFactory().createConnection();
        context.setConnection(connection);
                
        if (analyzer != null) {
            connection.setExceptionListener(new InterruptedConnectionListener(analyzer, context));    
        } else {
            connection.setExceptionListener(listener);
        }
        
        Session producerSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        context.setProducerSession(producerSession);
        Destination destination;
        if(configuration.getDestination() == null) {
            destination = JMSUtils.createDestination(producerSession, configuration);
        } else {
            destination = configuration.getDestination();
        }
        
        context.setProducer(producerSession.createProducer(destination));
        context.createResponseProcessor(configuration);
        
        connection.start();
        
        if(log.isInfoEnabled())
            log.info("Connected with " + configuration + ", context = " + context);
        return context;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
     *      java.lang.reflect.Method, java.lang.Object[])
     */
    public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
		if ("hashCode".equals(method.getName())) {
			return method.invoke(this, (Object[]) null);
		} else if ("equals".equals(method.getName())) {
			return method.invoke(this, params);
		}

    	RemoteCommand command = prepareRemoteCommand(proxy, method, params);
        if(log.isDebugEnabled())
            checkThreadConcurrency(command);
        
        // prepare and destroy method pair ensure always correct connection
        // handling... if pooling necessary put it on corresponding connections
        // and sessions...
        InvocationContext context = prepare();
        
        IInvocationCollector collector = CollectorUtils.connectJMXCollector(context.getJmxConnector(), configuration);
        InvocationInfo info = null;
        long start = System.currentTimeMillis();
        try {
            if(collector != null && collector.shouldCollect()) {
                info = CollectorUtils.addCollectorInfo(command);
                info.setSide(Side.CLIENT.name());
                context.setInfo(info);
            }
    
            if (log.isInfoEnabled())
                log.info("Invoking " + command);
            Response response = reconnectableSendReceiveResponse(command, configuration.getInvocationTimeout());
    
            if(collector != null && collector.shouldCollect()) {
                CollectorUtils.finalizeCollectorInfo(info, response, start);
            }
            if (response == null) {
                RemoteInvocationException exception = new RemoteInvocationException("Nobody answered within " + configuration.getInvocationTimeout()
                        + " timeout to method invocation = " + command);
                if(collector != null && collector.shouldCollect()) {
                    Response timeoutResponse = new Response(null, exception);
                    info.setResponse(timeoutResponse);
                }
                throw exception;
            }
    
            if (log.isInfoEnabled())
                log.info("Responding in (" + (System.currentTimeMillis() - start) + " ms) " + response);        
            
            if (response.getInvocationException() != null)
                throw response.getInvocationException();
    
            return response.getReturnValue();
        }finally {
            if(collector != null && collector.shouldCollect()) {
                collector.addInvocationInfo(info);
            }
            destroy();
        }
    }
    
    private Response reconnectableSendReceiveResponse(RemoteCommand command, long waitTimeout) throws Exception {
        InvocationContext context = threadInvocationContext.get();
        Response response = null;
        
        for (int i = 0; i < 100; i++) {

            if(log.isDebugEnabled())
                log.debug("Retrying to reconnect while sending message ...");

            try {
                if(i != 0) {
                    if(!analyzer.reconnect()) {
                        if(log.isDebugEnabled())
                            log.debug("Sleeping PS for " + configuration.getInvocationTimeout() + " ms.");
                       logAndSleep(configuration);
                       continue;
                    } else {
                        context = prepare();   
                    }
                }

                response = context.getResponseProcessor().sendReceiveResponse(command, configuration.getInvocationTimeout());
                if(i == 0 && context.isTransportAsynchronouslyInterrupted()) {
                    // transport was interrupted while invocation and we are still able to react
                    context.setTransportAsynchronouslyInterrupted(false);
                    logAndSleep(configuration);
                    continue;
                }

                if(log.isDebugEnabled())
                    log.debug("Message sent succesfully after reconnect.");
                
                return response;
            } catch (JMSException e) {
                if (analyzer != null && analyzer.isJMSExceptionSayingTransportInterrupted(e)) {
                    logAndSleep(configuration);
                    context.setTransportAsynchronouslyInterrupted(false);
                } else {
                    throw e;
                }
            }
        }
        throw new RemoteInvocationException("Unable to reconnect to JMS.");
    }
    
    private void logAndSleep(JMSBridgeConfiguration configuration) throws InterruptedException {
        if(log.isInfoEnabled())
            log.info("Stopping invocation handler and waiting " + configuration.getReconnectDelay() + "ms");
        Thread.sleep(configuration.getReconnectDelay());
    }
    
    private RemoteCommand prepareRemoteCommand(Object proxy, Method method, Object[] params) {
        return new RemoteCommand(proxy.getClass().getName(), method.getName(), params, method
                .getParameterTypes());
    }    

    public void onException(JMSException arg0) {
        log.error("JMS onException (" + arg0.getErrorCode() + ")", arg0);
        if(arg0.getLinkedException() != null)
            log.error("Linked exception ", arg0.getLinkedException());
    }
    
    /**
     * TODO find another way to "finalize" this object cause different Java VM implementations (Windows, AIX, ...)
     * can handle it in different ways (or not even call it). 
     */
    @Override
    protected void finalize() throws Throwable {
        if(log.isDebugEnabled())
            log.debug("Finalizing JMS Invocation handler");
        destroy();
        super.finalize();
    }
    
    /**
     * Properly shut-down the JMS connection for this invocation handler and all stateless variables.
     */
    private void destroy() {
        currentThread = null;
        threadInvocationContext.get().destroy();
    }

    /**
     * JMS exception analyzer capable of detecting transport interrupts. If set,
     * invocation handler will try to reconnect.
     * 
     * @param analyzer
     */
    public void setAnalyzer(IJMSExceptionAnalyzer analyzer) {
        this.analyzer = analyzer;
    }
}
