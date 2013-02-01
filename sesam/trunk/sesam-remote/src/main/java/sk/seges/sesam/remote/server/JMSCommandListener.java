/**
 * 
 */
package sk.seges.sesam.remote.server;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.InvalidDestinationException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.management.remote.JMXConnector;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import sk.seges.sesam.remote.domain.Constants;
import sk.seges.sesam.remote.domain.EJMSResponseHandling;
import sk.seges.sesam.remote.domain.JMSBridgeConfiguration;
import sk.seges.sesam.remote.domain.RemoteCommand;
import sk.seges.sesam.remote.domain.Response;
import sk.seges.sesam.remote.exception.RemoteInvocationException;
import sk.seges.sesam.remote.jmx.IInvocationCollector;
import sk.seges.sesam.remote.jmx.domain.InvocationInfo;
import sk.seges.sesam.remote.jmx.domain.Side;
import sk.seges.sesam.remote.jmx.util.CollectorUtils;
import sk.seges.sesam.remote.server.response.IResponseMessageBuilder;
import sk.seges.sesam.remote.server.response.StaticQueueResponseMessageBuilder;
import sk.seges.sesam.remote.server.response.TempQueueResponseMessageBuilder;
import sk.seges.sesam.remote.util.JMSUtils;


/**
 * @author AAlac
 * @author eldzi
 */
public class JMSCommandListener<T> implements MessageListener, ExceptionListener, ICommandListener {
    private static final Logger log = Logger.getLogger(JMSCommandListener.class);
    private T invocationTarget;
    
    private final JMSBridgeConfiguration configuration;
    private final ConnectionFactory factory;
    private Connection connection;
    private Session consumerSession;
    private Session producerSession;
    private Destination destination;
    private MessageConsumer consumer;
    
    /** Cached for reconnection purposes. */
    private ExceptionListener exceptionListener;
    
    private InvocationInfo info = null;
    
    private JMXConnector connector;
    private IResponseMessageBuilder responseMessageBuilder;
    
    public JMSCommandListener(JMSBridgeConfiguration configuration, T invocationTarget) throws JMSException {
        this(configuration, invocationTarget, false);
    }

    public JMSCommandListener(JMSBridgeConfiguration configuration, T invocationTarget, boolean addDefaultExceptionListener) throws JMSException {
        this.configuration = configuration;
        this.invocationTarget = invocationTarget;        

        factory = configuration.getFactory();
        if(addDefaultExceptionListener) {
        	exceptionListener = this;
        }
        
        if(configuration.isStartListenerAutomatically()) {
        	start();
        }
    }
    
    public void start() {
        try {
        	if(connection != null) {
                connection.close();
                connection = null;
            }
            connection = factory.createConnection();
            if(exceptionListener != null) {
            	connection.setExceptionListener(exceptionListener);
            }
            
            producerSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            consumerSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            if(configuration.getDestination() == null)
                destination = JMSUtils.createDestination(consumerSession, configuration);
            else
                destination = configuration.getDestination();
            
            if(log.isInfoEnabled())
                log.info("Listening with " + configuration);

            if(configuration.getDestinationSelector() == null || configuration.getDestinationSelector().isEmpty()) {
            	consumer = consumerSession.createConsumer(destination);
            } else {
            	consumer = consumerSession.createConsumer(destination, Constants.DESTINATION_SELECTOR + " = '" + configuration.getDestinationSelector() + "'");
            }
            consumer.setMessageListener(this);
            
            prepareResponseMessageBuilder();
            connection.start();
            
            if(CollectorUtils.activateJMX(configuration)) {
	            try {
	                connector = CollectorUtils.establishConnection(configuration.getJmxServiceURL());
	            } catch (IOException e) {
	                log.warn("JMX connector in command listener deactivated because of exception with configuration = " + configuration, e);
	                connector = null;
	            }
	        }
		} catch (JMSException e) {
			throw new RuntimeException("Cannot start JMS listener with config = " + configuration + " on target = " + invocationTarget, e);
		}
    }
    
    private void prepareResponseMessageBuilder() {
        if(EJMSResponseHandling.TEMPORARY_QUEUE.equals(configuration.getResponseHandling())) {
            responseMessageBuilder = new TempQueueResponseMessageBuilder();
        } else if(EJMSResponseHandling.STATIC_QUEUE.equals(configuration.getResponseHandling())) {
            responseMessageBuilder = new StaticQueueResponseMessageBuilder();
        } else {
            throw new RuntimeException("Unsupported response handling type " + configuration.getResponseHandling() + ". Use one of " + Arrays.toString(EJMSResponseHandling.values()));
        }
        
        responseMessageBuilder.setProducerSession(producerSession);
    }
    
    /**
     * Custom JMS exception handler if default isn't sufficient. Default handler is just logging the exception.
     * @param listener
     * @throws JMSException
     * @see {@link Connection#setExceptionListener(ExceptionListener)}
     */
    public void setJMSExceptionListener(ExceptionListener listener) throws JMSException {
        this.exceptionListener = listener;
        if(connection != null && !connection.getExceptionListener().equals(listener)) {
            // set it for existing connection, for non-existing it is set in startListening
            connection.setExceptionListener(listener);
        }
    }
    
    /* (non-Javadoc)
     * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
     */
    public void onMessage(Message message) {
        IInvocationCollector collector = CollectorUtils.connectJMXCollector(connector, configuration);
        
        long start = System.currentTimeMillis();
        if(!(message instanceof ObjectMessage))
            return;

        Serializable returnValue = null;
        
        Response response = null;
        try {
            RemoteCommand command = (RemoteCommand)((ObjectMessage) message).getObject();
            if(collector != null && collector.shouldCollect()) {
                info = CollectorUtils.addCollectorInfo(command);
                info.setSide(Side.SERVER.name());
                info.setUuid(responseMessageBuilder.getResponseUUID(message));
            }
            
            if (log.isInfoEnabled())
                log.info("Received " + command);
            
            Method method = invocationTarget.getClass().getMethod(command.getMethodName(), command.getParamTypes());
            Object ret = method.invoke(invocationTarget, command.getParams());
            if(ret != null && !(ret instanceof Serializable)) {
                returnValue = null;
                throw new RemoteInvocationException("Return value not serializable.", command);
            } else if(ret == null) {
                returnValue = null;
            } else {
                returnValue = (Serializable) ret;
            }

            try {
                response = sendResponse(message, returnValue, null);
            } catch(InvalidDestinationException ide) {
                response = new Response(returnValue, new RemoteInvocationException("Client has terminated response connection.", ide));
                log.warn("Client has terminated response connection.", ide);
            } catch (JMSException e) {
                response = new Response(returnValue, e);
                JMSUtils.logJMSException(log, "Cannot send return value response = " + returnValue, e);
            } finally {
                if(collector != null && collector.shouldCollect()) {
                    CollectorUtils.finalizeCollectorInfo(info, response, start);
                }
            }
        } catch (JMSException e) {
            response = new Response(returnValue, e);
            JMSUtils.logJMSException(log, "Cannot receive JMS message.", e);
        } catch (Exception e) {
        	Exception invocationException = e;
            if(invocationException instanceof InvocationTargetException) {
                invocationException = (Exception)((InvocationTargetException) invocationException).getTargetException();
            }
            try {
                response = sendResponse(message, null, invocationException);
            } catch (JMSException e1) {
                response = new Response(returnValue, e1);
                JMSUtils.logJMSException(log, "Cannot send exception response = " + invocationException.getClass().getName(), e1);
            }
        } finally {
            if(collector != null && collector.shouldCollect()) {
                CollectorUtils.finalizeCollectorInfo(info, response, start);
                collector.addInvocationInfo(info);
            }

            if (log.isInfoEnabled()) {
            	if(response == null) {
            		log.error("Weird, response shouldn't be null. Thread = " + Thread.currentThread() + ", message = " + message);
            	} else {
	            	if(response.getInvocationException() != null) {
	            		log.info("Sent response in (" + (System.currentTimeMillis() - start) + " ms) " + response, response.getInvocationException());
	            	} else {
	            		log.info("Sent response in (" + (System.currentTimeMillis() - start) + " ms) " + response);
	            	}
            	}
            }
        }
    }
    
    private Response sendResponse(Message message, Serializable returnValue, Throwable invocationException)
            throws JMSException {        
        Destination reply = message.getJMSReplyTo();
        if(log.isDebugEnabled())
            log.debug("Replying to temp. topic = " + reply + " with returnValue = " + returnValue, invocationException);
        if(reply == null)
            return null;

        MessageProducer producer = producerSession.createProducer(reply);
        producer.setDeliveryMode(configuration.getDeliveryMode());
        Response response = new Response(returnValue, invocationException);
        Message m = responseMessageBuilder.createMessage(message, response);
        m.setJMSExpiration(configuration.getInvocationTimeout());
        if(configuration.getDestinationSelector() != null && !configuration.getDestinationSelector().isEmpty()) {
        	m.setStringProperty(Constants.DESTINATION_SELECTOR, configuration.getDestinationSelector());
        }
        
        producer.send(m);
        producer.close();
        
        if(log.isDebugEnabled()) {
        	log.debug("Created response = " + response);
        }
        return response;
    }
    
    /**
	 * Properly shut-down the JMS connection for this invocation handler.
	 */
	public void destroy() {
		if (connector != null) {
			try {
				connector.close();
			} catch (IOException e) {
				log.warn("Error closing JMX connector.", e);
				connector = null;
			}
		}

		try {
			if (consumer != null) {
				consumer.close();
				consumer = null;
			}
		} catch (JMSException e) {
			log.warn("Error closing 'JMS MessageConsumer'.", e);
			consumer = null;
		}
		try {
			if (consumerSession != null) {
				consumerSession.close();
				consumerSession = null;
			}
		} catch (JMSException e) {
			log.warn("Error closing 'JMS consumer Session'.", e);
			consumerSession = null;
		}
		try {
			if (producerSession != null) {
				producerSession.close();
				producerSession = null;
			}
		} catch (JMSException e) {
			log.warn("Error closing 'JMS producer Session'.", e);
			producerSession = null;
		}
		try {
			if (connection != null) {
				connection.close();
				connection = null;
			}
		} catch (JMSException e) {
			log.warn("Error closing 'JMS Connection'.", e);
			connection = null;
		}
	}
    
    @Override
    protected void finalize() throws Throwable {
    	destroy();
        super.finalize();
    }

    public void onCommand(Object arg0) {
        if(log.isDebugEnabled())
            log.debug("onCommand " + arg0);
    }

    public void onException(IOException arg0) {
        if(log.isEnabledFor(Level.WARN))
            log.warn("onException ", arg0);
    }

    public void transportInterupted() {
        if(log.isEnabledFor(Level.WARN))
            log.warn("transportInterupted");
    }

    public void transportResumed() {
        if(log.isInfoEnabled())
            log.info("transportInterupted");
    }

    public void onException(JMSException arg0) {
        log.error("JMS onException (" + arg0.getErrorCode() + ")", arg0);
        if(arg0.getLinkedException() != null)
            log.error("Linked exception ", arg0.getLinkedException());
    }    
}
