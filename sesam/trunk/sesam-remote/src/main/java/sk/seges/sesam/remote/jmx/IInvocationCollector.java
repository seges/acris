/**
 * 
 */
package sk.seges.sesam.remote.jmx;

import sk.seges.sesam.remote.domain.ProtocolConfiguration;
import sk.seges.sesam.remote.jmx.domain.InvocationInfo;

/**
 * @author LGazo
 */
public interface IInvocationCollector {
    boolean shouldCollect();
    void addInvocationInfo(InvocationInfo info);
    void setConfiguration(ProtocolConfiguration configuration);
}
