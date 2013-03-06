/**
 * 
 */
package sk.seges.sesam.remote.domain;

import java.io.Serializable;

/**
 * @author AAlac
 * @author eldzi
 */
public class Response implements Serializable {
    private static final long serialVersionUID = 6003754165527436340L;
    
    private final Serializable returnValue;
    private final Throwable invocationException;

    public Response(Serializable returnValue, Throwable invocationException) {
        this.returnValue = returnValue;
        this.invocationException = invocationException;
    }

    public Serializable getReturnValue() {
        return returnValue;
    }

    public Throwable getInvocationException() {
        return invocationException;
    }
    
    @Override
    public String toString() {
        return "Response [ returnValue = " + returnValue + ", invocationEx = " + invocationException + "]";
    }
}
