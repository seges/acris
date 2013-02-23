/**
 * 
 */
package sk.seges.sesam.remote.jmx;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.openmbean.OpenDataException;

import sk.seges.sesam.remote.domain.ProtocolConfiguration;
import sk.seges.sesam.remote.exception.RemoteInvocationException;
import sk.seges.sesam.remote.jmx.domain.Side;
import sk.seges.sesam.remote.jmx.util.CollectorUtils;

/**
 * JMX Agent for Remote Invocation. To use it you just need to instantiate it
 * and assign configuration. Each configuration user should detect presence of
 * JMX inspecting configuration.
 * 
 * @author eldzi
 */
public class Agent {
	private final MBeanServer server;
	private XInvocationCollector handlerMBean;

	public Agent() {
		server = ManagementFactory.getPlatformMBeanServer();
	}

    /**
     * Constructor for JMX Agent that will also register on JMX server.
     * @param configuration
     * @param side
     */
    public Agent(ProtocolConfiguration configuration, String side) {
        this(configuration, Side.valueOf(side));
    }

	/**
     * Constructor for JMX Agent that will also register on JMX server.
     * @param configuration
     * @param side
     */
    public Agent(ProtocolConfiguration configuration, Side side) {
        this();
        registerInvocationCollector(configuration, side);
    }
    
    public void registerInvocationCollector(ProtocolConfiguration configuration, Side side) {
        try {
            handlerMBean = new XInvocationCollector();
            handlerMBean.setSide(side);
        } catch (OpenDataException e) {
            throw new RemoteInvocationException("Cannot create mbean", e);
        }
        handlerMBean.setConfiguration(configuration);
        
        ObjectName name = null;
        try {
            String id = java.util.UUID.randomUUID().toString();
            name = CollectorUtils.constructName(side, id);
            server.registerMBean(handlerMBean, name);
            configuration.setJmxCollector(name.getCanonicalName());
        } catch (Exception e) {
            throw new RemoteInvocationException("Cannot register " + side + " side mbean with name " + name, e);
        }
    }
}
