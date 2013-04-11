/**
 * 
 */
package sk.seges.sesam.remote.client.response;

import java.util.Date;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;

import org.apache.log4j.Logger;

import sk.seges.sesam.remote.client.ResponseListener;
import sk.seges.sesam.remote.domain.Constants;
import sk.seges.sesam.remote.domain.InvocationContext;
import sk.seges.sesam.remote.domain.JMSBridgeConfiguration;
import sk.seges.sesam.remote.domain.RemoteCommand;
import sk.seges.sesam.remote.domain.Response;

/**
 * @author eldzi
 */
public abstract class AbstractResponseProcessor {
    private static final Logger log = Logger.getLogger(AbstractResponseProcessor.class);
    
    protected InvocationContext context;
    protected JMSBridgeConfiguration configuration;
    
    protected Destination responseQueue;
    protected long time;
    
    public void setContext(InvocationContext context) {
        this.context = context;
    }
    
    public void setConfiguration(JMSBridgeConfiguration configuration) {
        this.configuration = configuration;
    }
    
    protected abstract Destination prepareResponseDestination() throws JMSException;
    protected void clean() throws JMSException { }
    protected abstract MessageConsumer createConsumer(InvocationContext context, MessageListener responseListener, Destination tempQueue) throws JMSException;
    protected void addPropertiesToMessage(Message msg) throws JMSException {};
    
    public Response sendReceiveResponse(RemoteCommand command, long waitTimeout) throws Exception {
        final Object lock = new Object();
        Response response = null;
        
        time = System.currentTimeMillis();
        
        context.createConsumerSession();
        responseQueue = prepareResponseDestination();
        if(responseQueue == null) {
        	throw new RuntimeException("No response destination specified ! command = " + command + ", configuration = " + configuration);
        }
        
        ResponseListener responseListener = new ResponseListener(lock);
        MessageConsumer consumer = createConsumer(context, responseListener, responseQueue);

        if(log.isInfoEnabled())
            log.info("Message sent" + "(" + new Date(time - System.currentTimeMillis()) + ")");
        synchronized (lock) {
            send(context, command, null, responseQueue);
            lock.wait(waitTimeout);
            response = responseListener.getReturnValue();
        }

        consumer.close();
        clean();
        context.getConsumerSession().close();
        return response;
    }

    private void send(InvocationContext context, RemoteCommand command, Integer priority, Destination reply) throws JMSException {
        MessageProducer producer = context.getProducer();
        producer.setDeliveryMode(configuration.getDeliveryMode());

        producer.setTimeToLive(configuration.getInvocationTimeout());

        if (priority != null) {
            // 0-4,5-9/def 4(spec)
            producer.setPriority(priority.intValue());
        }
        
        Message m = context.getProducerSession().createObjectMessage(command);
        m.setJMSExpiration(configuration.getInvocationTimeout());
        if(configuration.getDestinationSelector() != null && !configuration.getDestinationSelector().isEmpty()) {
        	m.setStringProperty(Constants.DESTINATION_SELECTOR, configuration.getDestinationSelector());
        }
        
        addPropertiesToMessage(m);
        
        if (reply != null) {
            m.setJMSReplyTo(reply);
        }
        producer.send(m);
    }
}
