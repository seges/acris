/**
 * 
 */
package sk.seges.sesam.remote.jmx.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import sk.seges.sesam.remote.domain.ProtocolConfiguration;
import sk.seges.sesam.remote.domain.RemoteCommand;
import sk.seges.sesam.remote.domain.Response;
import sk.seges.sesam.remote.exception.RemoteInvocationException;
import sk.seges.sesam.remote.jmx.IInvocationCollector;
import sk.seges.sesam.remote.jmx.domain.InvocationInfo;
import sk.seges.sesam.remote.jmx.domain.Side;

/**
 * @author eldzi
 */
public class CollectorUtils {
    public static InvocationInfo addCollectorInfo(RemoteCommand command) throws UnknownHostException {
        InvocationInfo info = new InvocationInfo();
        info.setClientHost(InetAddress.getLocalHost());
        info.setCommand(command);
        info.setInvocationStart(new Date());
        
        return info;
    }
    
    public static void finalizeCollectorInfo(InvocationInfo info, Response response, long start) {
        info.setResponse(response);
        info.setInvocationEnd(new Date());
        info.setInvocationDuration(System.currentTimeMillis() - start);
    }
    
    public static ObjectName constructName(Side side) throws MalformedObjectNameException {
        return new ObjectName("remoteInvocation:side=" + side);
    }
    
    public static ObjectName constructName(Side side, String id) throws MalformedObjectNameException {
        return new ObjectName("remoteInvocation:side=" + side + ",id=" + id);
    }
    
    public static IInvocationCollector retrieveCollector(String objectName, MBeanServerConnection connection) throws MalformedObjectNameException, NullPointerException {
        IInvocationCollector collector = (IInvocationCollector)MBeanServerInvocationHandler.newProxyInstance(connection, new ObjectName(objectName), IInvocationCollector.class, false);
        return collector;
    }
    
    public static JMXConnector establishConnection(String serviceURL) throws IOException {
        JMXServiceURL url =
            new JMXServiceURL(serviceURL);
        JMXConnector jmxc = JMXConnectorFactory.connect(url, null);

        return jmxc;
    }
    
    public static boolean activateJMX(ProtocolConfiguration configuration) {
        return configuration.getJmxCollector() != null && configuration.getJmxServiceURL() != null
                && configuration.getJmxCollector().length() > 0 && configuration.getJmxServiceURL().length() > 0;
    }
    
    public static IInvocationCollector connectJMXCollector(JMXConnector jmxc, ProtocolConfiguration configuration) {
        if (jmxc == null || !activateJMX(configuration)) {
            return null;
        }
                
        IInvocationCollector collector;
        MBeanServerConnection connection;
        try {
            connection = jmxc.getMBeanServerConnection();
            collector = CollectorUtils.retrieveCollector(configuration.getJmxCollector(), connection);
        } catch (Exception e) {
            throw new RemoteInvocationException("Cannot connect and retrieve JMX collector for configuration = " + configuration, e);
        }
        return collector;
    }
}
