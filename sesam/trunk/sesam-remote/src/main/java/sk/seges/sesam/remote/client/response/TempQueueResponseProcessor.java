/**
 * 
 */
package sk.seges.sesam.remote.client.response;

import java.util.Date;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.TemporaryQueue;

import org.apache.log4j.Logger;

import sk.seges.sesam.remote.domain.InvocationContext;
import sk.seges.sesam.remote.jmx.domain.InvocationInfo;

/**
 * @author eldzi
 */
public class TempQueueResponseProcessor extends AbstractResponseProcessor {
    private static final Logger log = Logger.getLogger(TempQueueResponseProcessor.class);

    /* (non-Javadoc)
     * @see com.saf.remote.client.AbstractResponseProcessor#prepareResponseDestination()
     */
    @Override
    protected Destination prepareResponseDestination() throws JMSException {
        log.info("Starting creating temporaryQueue" + "(" + new Date(time) + ")");
        TemporaryQueue tempQueue = context.getConsumerSession().createTemporaryQueue();
        
        InvocationInfo info = context.getInfo();
        if(info != null) {
            info.setUuid(tempQueue.getQueueName());
        }
        
        if(log.isInfoEnabled())
            log.info("TemporaryQueue created (" + new Date(time - System.currentTimeMillis()) + ") = " + tempQueue.getQueueName());
        
        return tempQueue;
    }
    
    @Override
    protected void clean() throws JMSException {
        ((TemporaryQueue)responseQueue).delete();
    }
    
    @Override
    protected MessageConsumer createConsumer(InvocationContext context, MessageListener responseListener, Destination tempQueue) throws JMSException {
        MessageConsumer consumer = context.getConsumerSession().createConsumer(tempQueue);
        consumer.setMessageListener(responseListener);
        return consumer;
    }
}
