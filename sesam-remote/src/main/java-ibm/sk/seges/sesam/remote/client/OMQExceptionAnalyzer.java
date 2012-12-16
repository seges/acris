package sk.seges.sesam.remote.client;

import javax.jms.JMSException;

import org.apache.log4j.Logger;

/**
 * 
 * @author JMedojevic
 * 
 */
public class OMQExceptionAnalyzer extends AbstractJMSExceptionAnalyzer {

    private static final Logger log = Logger.getLogger(OMQExceptionAnalyzer.class);
    // OMQ EC - Open MQ Error Code
    private static final String OMQ_EC_ILLEGAL_STATE_EXCEPTION = "C4062";
    private static final String OMQ_EC_RECEIVED_GOODBYE_MESSAGE_FROM_BROKER = "C4056";
    private static final String OMQ_EC_CONNECTION_RESET = "C4002";

    // private static final String OMQ_EC_HA_BROKER_DOWN = "C4003";

    public boolean isJMSExceptionSayingTransportInterrupted(JMSException exception) {
        if (OMQ_EC_ILLEGAL_STATE_EXCEPTION.equals(exception.getErrorCode())) {
            if (log.isDebugEnabled())
                log.debug("Analyzed JMS Exception saying transport interruped exception.", exception);

            return true;
        }

        return false;
    }

    public boolean isTransportInterrupted(JMSException exception) {
        if (OMQ_EC_RECEIVED_GOODBYE_MESSAGE_FROM_BROKER.equals(exception.getErrorCode())
                || OMQ_EC_CONNECTION_RESET.equals(exception.getErrorCode())) {
            if (log.isDebugEnabled())
                log.debug("Analyzed OpenMQ transport interruped exception.", exception);

            return true;
        }

        return false;
    }

}
