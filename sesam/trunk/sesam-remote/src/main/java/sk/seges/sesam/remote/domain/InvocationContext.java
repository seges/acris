/**
 * 
 */
package sk.seges.sesam.remote.domain;

import java.io.IOException;
import java.util.Arrays;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.management.remote.JMXConnector;

import org.apache.log4j.Logger;

import sk.seges.sesam.remote.client.response.AbstractResponseProcessor;
import sk.seges.sesam.remote.client.response.StaticQueueResponseProcessor;
import sk.seges.sesam.remote.client.response.TempQueueResponseProcessor;
import sk.seges.sesam.remote.jmx.domain.InvocationInfo;

/**
 * JMS Context holding objects for current invocation assuring thread-safetiness of invoke method calls. 
 * TODO: rename to JMSInvocationContext and create an interface...
 * 
 * @author eldzi
 */
public class InvocationContext {
    private static final Logger log = Logger.getLogger(InvocationContext.class);
    
    private Connection connection;
    private Session producerSession;
    private Session consumerSession;
    private MessageProducer producer;
    private AbstractResponseProcessor responseProcessor;
    
    private InvocationInfo info = null;
    
    private boolean transportAsynchronouslyInterrupted = false;
    
    private JMXConnector jmxConnector;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Session getProducerSession() {
        return producerSession;
    }

    public void setProducerSession(Session producerSession) {
        this.producerSession = producerSession;
    }

    public Session getConsumerSession() {
        return consumerSession;
    }

    public void createConsumerSession() throws JMSException {
        consumerSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    public MessageProducer getProducer() {
        return producer;
    }

    public void setProducer(MessageProducer producer) {
        this.producer = producer;
    }

    public InvocationInfo getInfo() {
        return info;
    }

    public void setInfo(InvocationInfo info) {
        this.info = info;
    }

    public boolean isTransportAsynchronouslyInterrupted() {
        return transportAsynchronouslyInterrupted;
    }

    /**
     * Transport interrupted information received from exception listener.
     * @param transportAsynchronouslyInterrupted
     */
    public void setTransportAsynchronouslyInterrupted(boolean transportAsynchronouslyInterrupted) {
        this.transportAsynchronouslyInterrupted = transportAsynchronouslyInterrupted;
    }
    
    public JMXConnector getJmxConnector() {
        return jmxConnector;
    }

    public void setJmxConnector(JMXConnector jmxConnector) {
        this.jmxConnector = jmxConnector;
    }

    public AbstractResponseProcessor getResponseProcessor() {
        return responseProcessor;
    }
    
    public void createResponseProcessor(JMSBridgeConfiguration configuration) {
        if(EJMSResponseHandling.TEMPORARY_QUEUE.equals(configuration.getResponseHandling())) {
            responseProcessor = new TempQueueResponseProcessor();
        } else if(EJMSResponseHandling.STATIC_QUEUE.equals(configuration.getResponseHandling())) {
            responseProcessor = new StaticQueueResponseProcessor();
        } else {
            throw new RuntimeException("Unsupported response handling type " + configuration.getResponseHandling() + ". Use one of " + Arrays.toString(EJMSResponseHandling.values()));
        }
        
        responseProcessor.setConfiguration(configuration);
        responseProcessor.setContext(this);
    }
    
    public void destroy() {
        info = null;
        
        if(jmxConnector != null) {
            try {
                jmxConnector.close();
            } catch (IOException e) {
                log.warn("Error closing JMX connector.", e);
                jmxConnector = null;
            }
        }
        
        try {
            if (producer != null) {
                producer.close();
                producer = null;
            }
        } catch (JMSException e) {
            log.warn("Error closing 'JMS MessageProducer'.", e);
            producer = null;
        }
        try {
            if (producerSession != null) {
                producerSession.close();
                producerSession = null;
            }
        } catch (JMSException e) {
            log.warn("Error closing 'JMS Session'.", e);
            producerSession = null;
        }
        try {
            if (consumerSession != null) {
                consumerSession.close();
                consumerSession = null;
            }
        } catch (JMSException e) {
            log.warn("Error closing 'JMS Session'.", e);
            consumerSession = null;
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
}
