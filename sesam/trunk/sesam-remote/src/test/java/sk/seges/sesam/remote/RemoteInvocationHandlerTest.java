package sk.seges.sesam.remote;

import org.junit.Test;

public class RemoteInvocationHandlerTest {
	@Test
    public void testInvoke() throws Exception {
//        TestInterface ti = JMSInvocationHandler.createProxy(TestInterface.class);
//        ti.test1();
//        ti.test2();
    }
    
    public interface TestInterface {
        void test1();
        int test2();
        Integer test3(Boolean b);
    }
}
