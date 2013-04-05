/**
 * 
 */
package sk.seges.sesam.remote.domain;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author LGazo
 * @author AAlac
 */
public class RemoteCommand implements Serializable {
    private static final long serialVersionUID = 868010442737466113L;

    private final String proxyClass;
    private final String methodName;
    private final Object[] params;
    private final Class<?>[] paramTypes;

    public RemoteCommand(String proxyClass, String methodName, Object[] params, Class<?>[] paramTypes) {
        this.proxyClass = proxyClass;
        this.methodName = methodName;
        this.params = params;
        this.paramTypes = paramTypes;
    }

    public String getProxyClass() {
        return proxyClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getParams() {
        return params;
    }

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    @Override
    public String toString() {
        return "RemoteCommand [method = " + methodName + ", paramTypes = " + Arrays.toString(paramTypes)
                + ", params = " + Arrays.toString(params) + ", proxyClass = " + proxyClass + "]";
    }
}
