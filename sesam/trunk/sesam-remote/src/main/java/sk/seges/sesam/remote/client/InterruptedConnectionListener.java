/**
 * 
 */
package sk.seges.sesam.remote.client;

import javax.jms.JMSException;

import sk.seges.sesam.remote.domain.InvocationContext;

/**
 * @author LGazo
 *
 */
public class InterruptedConnectionListener extends AbstractAnalyzedExceptionListener {
    private final InvocationContext context;
    
    public InterruptedConnectionListener(IJMSExceptionAnalyzer analyzer, InvocationContext context) {
        super(analyzer);
        this.context = context;
    }

    /* (non-Javadoc)
     * @see javax.jms.ExceptionListener#onException(javax.jms.JMSException)
     */
    public void onException(JMSException exception) {
        if(!analyzer.isTransportInterrupted(exception)) {
            //not our problem, just log
            onExceptionDefault(exception);
            return;
        }

        context.setTransportAsynchronouslyInterrupted(true);
    }

}
