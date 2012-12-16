/**
 * 
 */
package sk.seges.sesam.remote.client;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

/**
 * @author eldzi
 */
public abstract class AbstractJMSExceptionAnalyzer implements IJMSExceptionAnalyzer {
    protected ConnectionFactory factory;
    
    public void setConnectionFactory(ConnectionFactory factory) {
        this.factory = factory;
    }
    
    public boolean reconnect() {
        try {
            if(factory == null)
                throw new IllegalArgumentException("Factory is null");
            Connection connection = factory.createConnection();
            if(connection == null)
                throw new IllegalArgumentException("Connection is null");
            connection.close();
            return true;
        } catch (JMSException e) {
            return false;
        }
    }
}
