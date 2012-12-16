/**
 * 
 */
package sk.seges.sesam.remote.server;

import javax.jms.JMSException;

import org.apache.log4j.Logger;

import sk.seges.sesam.remote.client.AbstractAnalyzedExceptionListener;
import sk.seges.sesam.remote.client.IJMSExceptionAnalyzer;
import sk.seges.sesam.remote.domain.JMSBridgeConfiguration;
import sk.seges.sesam.remote.exception.RemoteInvocationException;

/**
 * @author eldzi
 */
public class JMSCommandReconnectListener<T> extends AbstractAnalyzedExceptionListener {
    private static final Logger log = Logger.getLogger(JMSCommandReconnectListener.class);
    private final JMSCommandListener<T> commandListener;
    private final JMSBridgeConfiguration configuration;
    
    public JMSCommandReconnectListener(IJMSExceptionAnalyzer analyzer, JMSCommandListener<T> commandListener, JMSBridgeConfiguration configuration) {
        super(analyzer);
        this.commandListener = commandListener;
        this.configuration = configuration;
    }

    /* (non-Javadoc)
     * @see javax.jms.ExceptionListener#onException(javax.jms.JMSException)
     */
    public void onException(JMSException exception) {
        if(!analyzer.isTransportInterrupted(exception)) { 
            onExceptionDefault(exception);
            return;
        }
        
        if(log.isDebugEnabled())
            log.debug("Destroying command listener.");
        commandListener.destroy();
        for(int i = 0; i < 100; i++) {
            try {
                Thread.sleep(configuration.getReconnectDelay());
            } catch (InterruptedException e) {
                throw new RemoteInvocationException("Cannot sleep while trying to reconnect command listener.", e);
            }
            if(log.isDebugEnabled())
                log.debug("Checking JMS connectivity in " + getClass());
            if(analyzer.reconnect()) {
                try {
                    commandListener.start();
                } catch (Exception e) {
                    throw new RuntimeException("Unexpected exception while trying to reconnect command listener.", e);
                }
                if(log.isInfoEnabled())
                    log.info("Command listener reconnected.");
                return;
            }
        }   
        throw new RuntimeException("Unable to reconnect to JMS.");
        
        
    }
    
    

}
