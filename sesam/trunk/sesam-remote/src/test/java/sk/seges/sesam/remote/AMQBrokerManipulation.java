package sk.seges.sesam.remote;

import javax.jms.ConnectionFactory;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.store.memory.MemoryPersistenceAdapter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 
 * @author LGazo
 */
public class AMQBrokerManipulation implements IBrokerManipulation, ApplicationContextAware {
    protected BrokerService broker;
    private ApplicationContext context;
    
    public ConnectionFactory createFactory(String cfName) throws Exception {
        if(broker != null && broker.isStarted())
            broker.stop();
        broker = new BrokerService();
        broker.setBrokerName("fred");
        broker.setPersistenceAdapter(new MemoryPersistenceAdapter());
        broker.addConnector((String) context.getBean(cfName + "AMQConnector"));
        broker.start();
        
        return (ConnectionFactory) context.getBean(cfName);
    }

    public void tearDown() throws Exception {
        if(broker != null)
            broker.stop();
    }

    public BrokerService getBroker() {
        return broker;
    }

    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        this.context = arg0;
    }
}
