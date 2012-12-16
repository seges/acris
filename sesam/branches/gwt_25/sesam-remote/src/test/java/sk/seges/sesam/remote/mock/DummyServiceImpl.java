/**
 * 
 */
package sk.seges.sesam.remote.mock;

import java.util.LinkedList;
import java.util.List;

/**
 * @author LGazo
 * @author AAlac
 */
public class DummyServiceImpl implements IDummyService {

    /* (non-Javadoc)
     * @see com.saf.mock.ITestDummyService#test1()
     */
    public void test1() {
    }

    /* (non-Javadoc)
     * @see com.saf.mock.ITestDummyService#test2()
     */
    public int test2() {
        return 840;
    }

    /* (non-Javadoc)
     * @see com.saf.mock.ITestDummyService#test3(java.lang.Boolean)
     */
    public Integer test3(Boolean b) {
        return Integer.valueOf(42);
    }

    public List<String> test4(int count, String prefix) {
        List<String> strings = new LinkedList<String>();
        for(int i = count; i > 0; i--)
            strings.add(prefix + i);
        return strings;
    }

    public void testException() throws Exception {
        throw new Exception("Test exception");
    }

    public void testSleep() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
