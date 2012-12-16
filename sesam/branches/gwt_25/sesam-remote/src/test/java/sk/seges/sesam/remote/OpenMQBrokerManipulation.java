package sk.seges.sesam.remote;

import javax.jms.ConnectionFactory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Broker manipulation with stand-alone OpenMQ broker. Broker needs to be
 * running before tests are executed.
 * 
 * @author LGazo
 */
public class OpenMQBrokerManipulation implements IBrokerManipulation, ApplicationContextAware {
    private ApplicationContext context;

    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        this.context = arg0;
    }

    public ConnectionFactory createFactory(String cfName) throws Exception {
        return (ConnectionFactory) context.getBean(cfName);
    }

    public void tearDown() throws Exception {
    }
}
