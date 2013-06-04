/**
 * 
 */
package sk.seges.sesam.remote.client;

import java.util.concurrent.FutureTask;

import javax.jms.JMSException;

import org.apache.log4j.Logger;

import sk.seges.sesam.remote.exception.RemoteInvocationException;

/**
 * @author LGazo
 */
public class ReconnectableInvocationListener extends AbstractAnalyzedExceptionListener {
    private static final Logger log = Logger.getLogger(ReconnectableInvocationListener.class);
    private final FutureTask<?> task;
    private final long reconnectDelay;
    
    public ReconnectableInvocationListener(IJMSExceptionAnalyzer analyzer, FutureTask<?> task, long reconnectDelay) {
        super(analyzer);
        this.task = task;
        this.reconnectDelay = reconnectDelay;
    }

    public void onException(JMSException exception) {
        if(!analyzer.isTransportInterrupted(exception)) {
            //not our problem, just log
            onExceptionDefault(exception);
            return;
        }
        
        if(log.isInfoEnabled())
            log.info("Stopping invocation handler and waiting " + reconnectDelay + "ms");
//        if(!task.cancel(true)) {
//            log.error(new RemoteInvocationException("Cannot cancel running invocation, probably task already completed. Causing exception is contained here.", exception));
//        }
        
        for(int i = 0; i < 100; i++) {
            try {
                Thread.sleep(reconnectDelay);
            } catch (InterruptedException e) {
                throw new RemoteInvocationException("Cannot sleep while trying to reconnect invocation handler.", e);
            }
            if(analyzer.reconnect()) {
                task.run();
                if(log.isInfoEnabled())
                    log.info("Invocation handler reconnected.");
                return;
            }
        }
        throw new RemoteInvocationException("Unable to reconnect to JMS.");
    }

}
