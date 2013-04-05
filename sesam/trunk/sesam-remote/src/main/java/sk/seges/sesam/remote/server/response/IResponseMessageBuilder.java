/**
 * 
 */
package sk.seges.sesam.remote.server.response;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import sk.seges.sesam.remote.domain.Response;

/**
 * @author eldzi
 */
public interface IResponseMessageBuilder {
    void setProducerSession(Session producerSession);
    Message createMessage(Message receivedMessage, Response response) throws JMSException;
    String getResponseUUID(Message receivedMessage) throws JMSException;
}
