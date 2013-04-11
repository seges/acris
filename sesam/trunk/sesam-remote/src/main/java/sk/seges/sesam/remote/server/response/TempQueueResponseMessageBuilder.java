/**
 * 
 */
package sk.seges.sesam.remote.server.response;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;

import sk.seges.sesam.remote.domain.Response;

/**
 * @author eldzi
 */
public class TempQueueResponseMessageBuilder implements IResponseMessageBuilder {
    private Session producerSession;
    
    public void setProducerSession(Session producerSession) {
        this.producerSession = producerSession;
    }
    
    /* (non-Javadoc)
     * @see sk.seges.sesam.remote.server.response.IResponseMessageBuilder#createMessage(javax.jms.Message, com.saf.remote.domain.Response)
     */
    public Message createMessage(Message receivedMessage, Response response) throws JMSException {
        return producerSession.createObjectMessage(response);
    }

    public String getResponseUUID(Message receivedMessage) throws JMSException {
        return ((Queue)receivedMessage.getJMSReplyTo()).getQueueName();
    }
}
