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
public class DelayedCallOnTopicStressTest extends DelayedCallOnQueueStressTest {
    @Override
    protected JMSBridgeConfiguration createConfig() throws Exception {
    	JMSBridgeConfiguration config = new JMSBridgeConfiguration(createFactory("tcpConnectionFactory1"), "testDest", "TOPIC", 10000);
        config.setResponseHandling(EJMSResponseHandling.TEMPORARY_QUEUE);
        return config;
    }
}
