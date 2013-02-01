/**
 * 
 */
package sk.seges.sesam.remote.mock;

import java.util.List;

/**
 * @author LGazo
 * @author AAlac
 */
public interface IDummyService {
    void test1();
    int test2();
    Integer test3(Boolean b);
    List<String> test4(int count, String prefix);
    void testException() throws Exception;
    void testSleep();
}
