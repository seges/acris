/**
 * 
 */
package sk.seges.sesam.remote.stress;

import sk.seges.sesam.remote.domain.EJMSResponseHandling;
import sk.seges.sesam.remote.domain.JMSBridgeConfiguration;

/**
 * @author lgazo
 * @author aalac
 */
public class MegaCallOnQueueStressTest extends AbstractStressTestCase {
    int i = 0;
    
    @Override
    protected JMSBridgeConfiguration createConfig() throws Exception {
//        return new InvokeBridgeConfiguration(createFactory("tcp://localhost:64616"), "testDest", "QUEUE", 10000);
    	JMSBridgeConfiguration config = new JMSBridgeConfiguration(createFactory("tcpConnectionFactory1"), "testDest", "QUEUE", 10000);
        config.setResponseHandling(EJMSResponseHandling.TEMPORARY_QUEUE);
        return config;
    }

    public void testCallMethodOnProxy() throws Exception {
        AbstractStressMethodCalls methodCalls = new AbstractStressMethodCalls() {
            protected void callMethods() throws Exception {
                tds.test1();
//                tds.test2();
//                tds.test4(4, "ff");
                System.out.println(i++);
                Thread.sleep(100);
                
            }
        };
        
        callTest(500000, methodCalls, getClass().getName() + "_testClassMethodsOnProxy");
    }
}
