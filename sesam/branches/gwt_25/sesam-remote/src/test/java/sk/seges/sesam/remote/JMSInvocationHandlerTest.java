package sk.seges.sesam.remote;

import org.junit.Assert;

import sk.seges.sesam.remote.client.JMSInvocationHandler;
import sk.seges.sesam.remote.domain.EJMSResponseHandling;
import sk.seges.sesam.remote.domain.JMSBridgeConfiguration;
import sk.seges.sesam.remote.exception.RemoteInvocationException;
import sk.seges.sesam.remote.jmx.Agent;
import sk.seges.sesam.remote.mock.DummyServiceImpl;
import sk.seges.sesam.remote.mock.IDummyService;
import sk.seges.sesam.remote.server.JMSCommandListener;


public class JMSInvocationHandlerTest extends AbstractJMSTestCase {
    Agent agent;
    
    @Override
    protected void setUp() throws Exception {
        agent = new Agent();
    }
    
    public void testCreateProxyWithSpecificConfiguration() throws Exception {
        config = new JMSBridgeConfiguration(createFactory("vmConnectionFactory1"), "testDest", "QUEUE", 15000);
        ((JMSBridgeConfiguration) config).setResponseHandling(EJMSResponseHandling.TEMPORARY_QUEUE);
        IDummyService tds = new JMSInvocationHandler<IDummyService>((JMSBridgeConfiguration) config).createProxy(IDummyService.class);
        assertEquals(true, tds instanceof IDummyService);
    }
    
    public void testCreateProxyToTopic() throws Exception {
        config = new JMSBridgeConfiguration(createFactory("vmConnectionFactory1"), "testDest", "TOPIC", 15000);
        ((JMSBridgeConfiguration) config).setResponseHandling(EJMSResponseHandling.TEMPORARY_QUEUE);
        IDummyService tds = new JMSInvocationHandler<IDummyService>((JMSBridgeConfiguration) config).createProxy(IDummyService.class);
        Assert.assertEquals(true, tds instanceof IDummyService);
    }
    
    public void testInvokeVariousMethodsOnProxyWithoutListener() throws Exception {
        config = new JMSBridgeConfiguration(createFactory("vmConnectionFactory1"), "testDest", "QUEUE", 1000);
        ((JMSBridgeConfiguration) config).setResponseHandling(EJMSResponseHandling.TEMPORARY_QUEUE);
        IDummyService tds = new JMSInvocationHandler<IDummyService>((JMSBridgeConfiguration) config).createProxy(IDummyService.class);

        try {
            tds.test1();
        } catch(Throwable t) {
            assertEquals(true, t instanceof RemoteInvocationException);
            return;
        }
        fail("Call should be timeouted.");
    }
    
    public void testCallMethodsOnProxy() throws Exception {
        config = new JMSBridgeConfiguration(createFactory("vmConnectionFactory1"), "testDest", "QUEUE", 10000);
        ((JMSBridgeConfiguration) config).setResponseHandling(EJMSResponseHandling.TEMPORARY_QUEUE);
        IDummyService tds = new JMSInvocationHandler<IDummyService>((JMSBridgeConfiguration) config).createProxy(IDummyService.class);
        
        DummyServiceImpl tdsi = new DummyServiceImpl();
        new JMSCommandListener<IDummyService>((JMSBridgeConfiguration) config, tdsi);
        
//        agent.registerInvocationCollector(config, Side.SERVER);
        tds.test1();
        assertEquals(840, tds.test2());
        assertEquals(Integer.valueOf(42), tds.test3(Boolean.TRUE));
        int count = 4;
        assertEquals(count, tds.test4(count, "prefixTest").size());
        try {
            tds.testException();
        } catch(Exception e) {
            return;
        }
        fail("Call of test5 should throw exception.");
    }
    
    public void testCallMethodOnProxyWithTopicConfiguration() throws Exception {
        config = new JMSBridgeConfiguration(createFactory("vmConnectionFactory1"), "testDest", "TOPIC", 1000);
        ((JMSBridgeConfiguration) config).setResponseHandling(EJMSResponseHandling.TEMPORARY_QUEUE);
        IDummyService tds = new JMSInvocationHandler<IDummyService>((JMSBridgeConfiguration) config).createProxy(IDummyService.class);
        
        DummyServiceImpl tdsi = new DummyServiceImpl();
        new JMSCommandListener<IDummyService>((JMSBridgeConfiguration) config, tdsi);
        
        tds.test1();
    }
}
