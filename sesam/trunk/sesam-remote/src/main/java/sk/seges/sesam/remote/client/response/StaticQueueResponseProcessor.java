/**
 * 
 */
package sk.seges.sesam.remote.client.response;

import java.util.UUID;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

import sk.seges.sesam.remote.domain.Constants;
import sk.seges.sesam.remote.domain.InvocationContext;
import sk.seges.sesam.remote.domain.JMSBridgeConfiguration;
import sk.seges.sesam.remote.jmx.domain.InvocationInfo;

/**
 * @author LGazo
 */
public class StaticQueueResponseProcessor extends AbstractResponseProcessor {
    private String responseUUID = null;
    
    /* (non-Javadoc)
     * @see com.saf.remote.client.AbstractResponseProcessor#createConsumer(com.saf.remote.domain.InvocationContext, javax.jms.MessageListener, javax.jms.Destination)
     */
    @Override
    protected MessageConsumer createConsumer(InvocationContext context, MessageListener responseListener,
            Destination tempQueue) throws JMSException {
        MessageConsumer consumer;
		if (configuration.getDestinationSelector() == null
				|| configuration.getDestinationSelector().isEmpty()) {
			consumer = context.getConsumerSession().createConsumer(tempQueue,
					Constants.RESPONSE_UUID_KEY + " = '" + responseUUID + "'");
		} else {
			consumer = context.getConsumerSession().createConsumer(
					tempQueue,
					Constants.RESPONSE_UUID_KEY + " = '" + responseUUID
							+ "' and " + Constants.DESTINATION_SELECTOR
							+ " = '" + configuration.getDestinationSelector()
							+ "'");
		}
        consumer.setMessageListener(responseListener);
        return consumer;
    }

    /* (non-Javadoc)
     * @see com.saf.remote.client.AbstractResponseProcessor#prepareResponseDestination()
     */
    @Override
    protected Destination prepareResponseDestination() throws JMSException {
        InvocationInfo info = context.getInfo();
        responseUUID = UUID.randomUUID().toString();
        if(info != null) {
            info.setUuid(responseUUID);
        }
        
        return ((JMSBridgeConfiguration)configuration).getResponseDestination();
    }

    @Override
    protected void addPropertiesToMessage(Message msg) throws JMSException {
        msg.setStringProperty(Constants.RESPONSE_UUID_KEY, responseUUID);
    }
}
