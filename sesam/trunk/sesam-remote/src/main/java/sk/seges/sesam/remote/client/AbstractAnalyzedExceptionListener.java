/**
 * 
 */
package sk.seges.sesam.remote.client;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;

import org.apache.log4j.Logger;

/**
 * @author eldzi
 */
public abstract class AbstractAnalyzedExceptionListener implements ExceptionListener {
    private static final Logger log = Logger.getLogger(AbstractAnalyzedExceptionListener.class);
    protected final IJMSExceptionAnalyzer analyzer;

    public AbstractAnalyzedExceptionListener(IJMSExceptionAnalyzer analyzer) {
        super();
        this.analyzer = analyzer;
    }

    /**
     * Default action on exception receiving.
     * 
     * @param exception
     *            JMS Exception received on connection.
     */
    protected void onExceptionDefault(JMSException exception) {
        log.error("JMS onException (error code = " + exception.getErrorCode() + ")", exception);
        if (exception.getLinkedException() != null)
            log.error("Linked exception ", exception.getLinkedException());
    }
}
