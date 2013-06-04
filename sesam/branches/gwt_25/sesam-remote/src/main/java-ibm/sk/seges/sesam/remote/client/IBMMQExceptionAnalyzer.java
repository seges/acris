/**
 * 
 */
package sk.seges.sesam.remote.client;

import javax.jms.JMSException;

import com.ibm.mq.MQException;

/**
 * @author eldzi
 */
public class IBMMQExceptionAnalyzer extends AbstractJMSExceptionAnalyzer {
    private static final Logger log = Logger.getLogger(IBMMQExceptionAnalyzer.class);
    
    public boolean isTransportInterrupted(JMSException exception) {
        if (hasMQException(exception))
            return false;

        MQException mqException = (MQException) exception.getLinkedException();
        if (mqException.completionCode == MQException.MQCC_FAILED
                && (mqException.reasonCode == 2161 || mqException.reasonCode == 2162 || mqException.reasonCode == 2019 || mqException.reasonCode == 2059 || mqException.reasonCode == 2009)) {
            if(log.isDebugEnabled())
                log.debug("Analyzed IBM transport interruped exception.", mqException);
            return true;
        }

        return false;
    }

    public boolean isJMSExceptionSayingTransportInterrupted(JMSException exception) {
        if (hasMQException(exception))
            return false;

        MQException mqException = (MQException) exception.getLinkedException();
        if (mqException.completionCode == MQException.MQCC_FAILED
                && (mqException.reasonCode == 2019 || mqException.reasonCode == 2059 || mqException.reasonCode == 2009)) {
            if(log.isDebugEnabled())
                log.debug("Analyzed JMS Exception saying transport interruped exception.", mqException);
            return true;
        }

        return false;

    }
    
    /**
     * returns true when (exception.getLinkedException() == null) or (exception.getLinkedException() NOT instanceof MQException)
     * @param exception
     * @return
     */
    private boolean hasMQException(JMSException exception) {
        return exception.getLinkedException() == null || !(exception.getLinkedException() instanceof MQException);
    }
}
