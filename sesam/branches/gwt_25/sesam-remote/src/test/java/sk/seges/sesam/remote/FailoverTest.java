/**
 * 
 */
package sk.seges.sesam.remote;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

import sk.seges.sesam.remote.client.JMSInvocationHandler;
import sk.seges.sesam.remote.domain.EJMSResponseHandling;
import sk.seges.sesam.remote.domain.JMSBridgeConfiguration;
import sk.seges.sesam.remote.mock.DummyServiceImpl;
import sk.seges.sesam.remote.mock.IDummyService;
import sk.seges.sesam.remote.server.JMSCommandListener;

/**
 * @author LGazo
 * @author AAlac
 */
public class FailoverTest extends AbstractJMSTestCase {
    public void testCallMethodOnProxyWithInterruptedBroker() throws Exception {
        BrokerService brokerFO = new BrokerService();

        config = new JMSBridgeConfiguration(createFactory("tcpConnectionFactory2", brokerFO) , "testDest", "TOPIC", 1000);
        ((JMSBridgeConfiguration) config).setResponseHandling(EJMSResponseHandling.TEMPORARY_QUEUE);
        DummyServiceImpl tdsi = new DummyServiceImpl();
        new JMSCommandListener<IDummyService>((JMSBridgeConfiguration) config, tdsi);

        
        config = new JMSBridgeConfiguration(createFactory("tcpConnectionFactory1") , "testDest", "TOPIC", 1000);
        ((JMSBridgeConfiguration) config).setResponseHandling(EJMSResponseHandling.TEMPORARY_QUEUE);
        tdsi = new DummyServiceImpl();
        new JMSCommandListener<IDummyService>((JMSBridgeConfiguration) config, tdsi);

        
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("failover://(tcp://localhost:64616,tcp://localhost:64617)?initialReconnectDelay=100");
        config = new JMSBridgeConfiguration(factory , "testDest", "TOPIC", 1000);
        ((JMSBridgeConfiguration) config).setResponseHandling(EJMSResponseHandling.TEMPORARY_QUEUE);
        final IDummyService tds = new JMSInvocationHandler<IDummyService>((JMSBridgeConfiguration) config).createProxy(IDummyService.class);

        new Thread(new Runnable() {
            public void run() {
                tds.testSleep();
            }
        }).start();
        ((AMQBrokerManipulation)brokerManipulation).getBroker().stop();
        Thread.sleep(10000);
    }
}
