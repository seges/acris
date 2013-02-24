package sk.seges.sesam.remote.domain;

import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;

import sk.seges.sesam.remote.jmx.FieldExport;

/**
 * Configuration context class for remote invocation mechanism.
 * 
 * This configuration class holds all information required for establishing
 * successful communication channel between any participant node of remote
 * invocation mechanism. This configuration is specific for Java Messaging
 * System. It will provide underlying Java Messaging Broker with appropriate
 * parameters.
 * 
 * @author AAlac
 * @author eldzi
 */
public class JMSBridgeConfiguration implements ProtocolConfiguration {
    private final ConnectionFactory factory;
    @FieldExport(index = 2)
    private String destinationName;
    @FieldExport(index = 4)
    private long invocationTimeout = 15000;
    @FieldExport(index = 3, type = String.class, convertToString = true)
    private DestinationType destinationType;
    @FieldExport(index = 1, type = String.class, convertToString = true)
    private Destination destination;
    @FieldExport(type = String.class, convertToString = true)
    private Destination responseDestination;
    @FieldExport(type = String.class, convertToString = true)
    private EJMSResponseHandling responseHandling;
    /** Object name of JMX Collector. */
    @FieldExport
    private String jmxCollector;
    /** For JMX-RMI: service:jmx:rmi:///jndi/rmi://hostName:portNum/jmxrmi */
    @FieldExport
    private String jmxServiceURL;
    @FieldExport
    private long reconnectDelay = 3000;
    private boolean startListenerAutomatically;
	@FieldExport
    private int deliveryMode = DeliveryMode.NON_PERSISTENT;
	@FieldExport
	private String destinationSelector;
    /**
     * Constructor taking into account connection specific arguments.
     * 
     * @param factory
     *            JMS factory which will serve for purposes of creating JMS
     *            connections.
     * @param destination
     *            JMS Destination where remote invocation should listen.
     */
    public JMSBridgeConfiguration(ConnectionFactory factory, Destination destination) {
        this(factory, destination, true);
    }
    
    /**
     * Constructor taking into account connection specific arguments.
     * 
     * @param factory
     *            JMS factory which will serve for purposes of creating JMS
     *            connections.
     * @param destination
     *            JMS Destination where remote invocation should listen.
     */
    public JMSBridgeConfiguration(ConnectionFactory factory, Destination destination, boolean startListenerAutomatically) {
        this.factory = factory;
        this.destination = destination;
        this.startListenerAutomatically = startListenerAutomatically;
    }
    
    /**
     * Constructor taking into account connection specific arguments.
     * 
     * @param factory
     *            JMS factory which will serve for purposes of creating JMS
     *            connections.
     * @param destination
     *            JMS Destination where remote invocation should listen.
     * @param invocationTimeout
     *            General purpose timeout for connection responses. Usually it
     *            determines how long should a client wait until it states that
     *            method invocation wasn't successful.            
     */
    public JMSBridgeConfiguration(ConnectionFactory factory, Destination destination,
            long invocationTimeout, boolean startListenerAutomatically) {
        this(factory, destination, startListenerAutomatically);
        this.invocationTimeout = invocationTimeout;
    }
    
    /**
     * Constructor taking into account connection specific arguments.
     * 
     * @param factory
     *            JMS factory which will serve for purposes of creating JMS
     *            connections.
     * @param destinationName
     *            Destination name of connected JMS destination where remote
     *            invocation should listen.
     * @param destinationType
     *            Type of JMS destination according to JMS Specification.
     *            Nowadays there are two types supported - QUEUE and TOPIC.
     * @param invocationTimeout
     *            General purpose timeout for connection responses. Usually it
     *            determines how long should a client wait until it states that
     *            method invocation wasn't successful.
     */    
    public JMSBridgeConfiguration(ConnectionFactory factory, String destinationName, String destinationType,
            long invocationTimeout) {
    	this(factory, destinationName, destinationType, invocationTimeout, true);
    }
    
    public JMSBridgeConfiguration(ConnectionFactory factory, String destinationName, String destinationType,
            long invocationTimeout, boolean startListenerAutomatically) {
        this.factory = factory;
        this.destinationName = destinationName;
        this.invocationTimeout = invocationTimeout;
        this.destinationType = DestinationType.valueOf(destinationType.toUpperCase());
        this.startListenerAutomatically = startListenerAutomatically;
    }

    /**
     * @return Returns JMS connection factory associated with this configuration.
     */
    public ConnectionFactory getFactory() {
        return factory;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public long getInvocationTimeout() {
        return invocationTimeout;
    }

    public void setInvocationTimeout(long invocationTimeout) {
    	this.invocationTimeout = invocationTimeout;
    }
    
    public DestinationType getDestinationType() {
        return destinationType;
    }
    
    public boolean isStartListenerAutomatically() {
		return startListenerAutomatically;
	}

    public Destination getDestination() {
        return destination;
    }

    /**
     * Helper enumeration representing available JMS Destination types.
     * 
     * @author LGazo
     * @author AAlac
     */
    public static enum DestinationType {
        TOPIC, QUEUE;
    }

    public String getJmxCollector() {
        return jmxCollector;
    }

    public void setJmxCollector(String jmxCollector) {
        this.jmxCollector = jmxCollector;
    }

    public String getJmxServiceURL() {
        return jmxServiceURL;
    }
    
    public void setJmxServiceURL(String jmxURL) {
        this.jmxServiceURL = jmxURL;
    }
    
    public long getReconnectDelay() {
        return reconnectDelay;
    }

    public void setReconnectDelay(long reconnectDelay) {
        this.reconnectDelay = reconnectDelay;
    }
    
    public Destination getResponseDestination() {
        return responseDestination;
    }
    
    public void setResponseDestination(Destination responseDestination) {
        this.responseDestination = responseDestination;
    }
    
    public EJMSResponseHandling getResponseHandling() {
        return responseHandling;
    }
    
    public void setResponseHandling(EJMSResponseHandling responseHandling) {
        this.responseHandling = responseHandling;
    }
    
    public void setResponseHandling(String responseHandlingString) {
        this.responseHandling = EJMSResponseHandling.valueOf(responseHandlingString.toUpperCase());
    }
    
    public int getDeliveryMode() {
        return deliveryMode;
    }
    
    public void setDeliveryMode(int deliveryMode) {
        this.deliveryMode = deliveryMode;
    }
    
    public String getDestinationSelector() {
		return destinationSelector;
	}
    public void setDestinationSelector(String destinationSelector) {
		this.destinationSelector = destinationSelector;
	}

	@Override
	public String toString() {
		return "JMSBridgeConfiguration [factory=" + factory
				+ ", destinationName=" + destinationName
				+ ", invocationTimeout=" + invocationTimeout
				+ ", destinationType=" + destinationType + ", destination="
				+ destination + ", responseDestination=" + responseDestination
				+ ", responseHandling=" + responseHandling + ", jmxCollector="
				+ jmxCollector + ", jmxServiceURL=" + jmxServiceURL
				+ ", reconnectDelay=" + reconnectDelay
				+ ", startListenerAutomatically=" + startListenerAutomatically
				+ ", deliveryMode=" + deliveryMode + ", destinationSelector="
				+ destinationSelector + "]";
	}
    
    
}
