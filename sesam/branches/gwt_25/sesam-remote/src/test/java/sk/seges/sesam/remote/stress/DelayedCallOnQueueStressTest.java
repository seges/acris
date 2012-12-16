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
public class DelayedCallOnQueueStressTest extends AbstractStressTestCase {
    @Override
    protected JMSBridgeConfiguration createConfig() throws Exception {
    	JMSBridgeConfiguration config = new JMSBridgeConfiguration(createFactory("tcpConnectionFactory1"), "testDest", "QUEUE", 10000);
        config.setResponseHandling(EJMSResponseHandling.TEMPORARY_QUEUE);
        return config;
    }

    public void testCallMethodOnProxyDelayed() throws Exception {
        AbstractStressMethodCalls methodCalls = new AbstractStressMethodCalls() {
            protected void callMethods() throws Exception {
                tds.test1();
            }
            
            @Override
            protected void afterCallMethods() throws Exception {
                Thread.sleep(300);
            }
        };
        
        callTest(100, methodCalls, getClass().getName() + "testCallMethodOnProxyDelayed");
    }
}
