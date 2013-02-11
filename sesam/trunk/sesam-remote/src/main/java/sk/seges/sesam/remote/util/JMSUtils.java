package sk.seges.sesam.remote.util;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.log4j.Logger;

import sk.seges.sesam.remote.domain.JMSBridgeConfiguration;

/**
 * JMS util methods.
 * 
 * @author eldzi
 */
public final class JMSUtils {
	private JMSUtils() {}
	
    public static Destination createDestination(Session session, JMSBridgeConfiguration configuration) throws JMSException {
        if (configuration.getDestinationType() == JMSBridgeConfiguration.DestinationType.TOPIC)
            return session.createTopic(configuration.getDestinationName());
        else
            return session.createQueue(configuration.getDestinationName());
    }
    
    public static void logJMSException(Logger log, JMSException exception) {
        logJMSException(log,"JMS onException", exception);
    }
    
    public static void logJMSException(Logger log, String additionalMessage, JMSException exception) {
        log.error(additionalMessage + "; (error code = " + exception.getErrorCode() + ")", exception);
        if (exception.getLinkedException() != null){
            log.error("Linked exception ", exception.getLinkedException());
        }
    }
}
