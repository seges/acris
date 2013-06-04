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
public class CallOnQueueStressTest extends AbstractStressTestCase {
    @Override
    protected JMSBridgeConfiguration createConfig() throws Exception {
        JMSBridgeConfiguration config = new JMSBridgeConfiguration(createFactory("tcpConnectionFactory1"), "testDest", "QUEUE", 10000);
        config.setResponseHandling(EJMSResponseHandling.TEMPORARY_QUEUE);
        return config;
    }

    public void testCallMethodOnProxy() throws Exception {
        AbstractStressMethodCalls methodCalls = new AbstractStressMethodCalls() {
            protected void callMethods() throws Exception {
                tds.test1();
            }
        };
        
        callTest(300, methodCalls, getClass().getName() + "_testClassMethodsOnProxy");
    }
}
