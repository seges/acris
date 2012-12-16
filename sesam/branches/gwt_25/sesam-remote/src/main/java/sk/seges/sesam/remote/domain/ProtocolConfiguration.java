/**
 * 
 */
package sk.seges.sesam.remote.domain;


/**
 * General interface for protocol configuration defining all required attributes
 * available to each protocol implementation.
 * 
 * TODO: move JMX specific part to a child interface (i.e. JMXAwareProtocolConfiguration)
 * 
 * @author eldzi
 */
public interface ProtocolConfiguration {
	long getInvocationTimeout();
	
	/**
	 * JMX invocation collector for gathering invocation commands and inspecting
	 * them.
	 * 
	 * @return Protocol specific invocation collector.
	 */
    String getJmxCollector();

    void setJmxCollector(String jmxCollector);

    String getJmxServiceURL();
    
    void setJmxServiceURL(String jmxURL);
}
