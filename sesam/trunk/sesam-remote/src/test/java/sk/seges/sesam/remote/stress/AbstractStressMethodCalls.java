/**
 * 
 */
package sk.seges.sesam.remote.stress;

/**
 * @author lgazo
 * @author aalac
 */
public abstract class AbstractStressMethodCalls {
    protected abstract void callMethods() throws Exception;
    protected void afterCallMethods() throws Exception {};
}
