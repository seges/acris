/**
 * 
 */
package sk.seges.sesam.remote;

import javax.jms.ConnectionFactory;

import junit.framework.TestCase;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.store.memory.MemoryPersistenceAdapter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import sk.seges.sesam.remote.domain.ProtocolConfiguration;

/**
 * @author LGazo
 * @author AAlac
 */
public abstract class AbstractJMSTestCase extends TestCase {
    protected static IBrokerManipulation brokerManipulation;
    protected ProtocolConfiguration config;
    private ApplicationContext context;
    
    public AbstractJMSTestCase() {
        if(getClass().getClassLoader().getResource("testApplicationContext-embedded.xml") == null) {
            context = new ClassPathXmlApplicationContext("/testApplicationContext.xml");
        } else {
            context = new ClassPathXmlApplicationContext(new String[] {"/testApplicationContext.xml", "/testApplicationContext-embedded.xml"});
        }
        setBrokerManipulation((IBrokerManipulation) context.getBean("brokerManipulation"));
    }
    
    public void setBrokerManipulation(IBrokerManipulation brokerManipulation) {
        AbstractJMSTestCase.brokerManipulation = brokerManipulation;
    }
    
    @Override
    protected void tearDown() throws Exception {
        brokerManipulation.tearDown();
    }
    
    protected ConnectionFactory createFactory(String cfName) throws Exception {
        return brokerManipulation.createFactory(cfName);
    }
    
    protected ActiveMQConnectionFactory createFactory(String cfName, BrokerService broker) throws Exception {
        broker.setBrokerName("john");
        broker.setPersistenceAdapter(new MemoryPersistenceAdapter());
        broker.addConnector((String) context.getBean(cfName + "AMQConnector"));
        broker.start();
        
        return (ActiveMQConnectionFactory) context.getBean(cfName);
    }
}
