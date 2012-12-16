package sk.seges.sesam.remote;

import javax.jms.ConnectionFactory;

/**
 * Interface defining common methods for manipulating with a broker needed for
 * JMS test cases.
 * 
 * @author LGazo
 */
public interface IBrokerManipulation {
    /**
     * What to do when test case goes down.
     * 
     * @throws Exception
     */
    void tearDown() throws Exception;

    /**
     * Creates JMS connection factory defined by a name. Broker manipulation
     * implementation is in charge of picking the right one.
     * 
     * @param cfName
     * @return
     * @throws Exception
     */
    ConnectionFactory createFactory(String cfName) throws Exception;
}
