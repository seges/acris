package sk.seges.sesam.remote.client;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

public interface IJMSExceptionAnalyzer {
    void setConnectionFactory(ConnectionFactory factory);
    boolean isTransportInterrupted(JMSException exception);
    boolean isJMSExceptionSayingTransportInterrupted(JMSException exception);
    boolean reconnect();
}
