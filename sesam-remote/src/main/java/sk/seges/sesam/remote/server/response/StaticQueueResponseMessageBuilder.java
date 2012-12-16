/**
 * 
 */
package sk.seges.sesam.remote.server.response;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import sk.seges.sesam.remote.domain.Constants;
import sk.seges.sesam.remote.domain.Response;

/**
 * @author eldzi
 */
public class StaticQueueResponseMessageBuilder implements IResponseMessageBuilder {
    private Session producerSession;
    
    public void setProducerSession(Session producerSession) {
        this.producerSession = producerSession;
    }
    
    /* (non-Javadoc)
     * @see sk.seges.sesam.remote.server.response.IResponseMessageBuilder#createMessage(javax.jms.Message, com.saf.remote.domain.Response)
     */
    public Message createMessage(Message receivedMessage, Response response) throws JMSException {
        Message message = producerSession.createObjectMessage(response);
        message.setStringProperty(Constants.RESPONSE_UUID_KEY, getResponseUUID(receivedMessage));
        return message;
    }

    public String getResponseUUID(Message receivedMessage) throws JMSException {
        return receivedMessage.getStringProperty(Constants.RESPONSE_UUID_KEY);
    }
}
