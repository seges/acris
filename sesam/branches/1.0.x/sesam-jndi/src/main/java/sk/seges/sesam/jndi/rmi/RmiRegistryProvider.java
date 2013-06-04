package sk.seges.sesam.jndi.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

/**
 * registry for RMI can be set via jndi (
 * &lt;Resource auth="Container" name="rmi/reporting"
 * factory="sk.seges.sesam.jndi.rmi.RmiRegistryProvider"
 * type="java.rmi.registry.Registry" port="11991" /&gt;
 * )<br />
 * used instead of RmiRegistryFactoryBean
 * 
 * @author marta
 * 
 */
public class RmiRegistryProvider implements ObjectFactory {

	private boolean alwaysCreate = false;

	private int port = 1099;

	@Override
	public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment)
			throws Exception {
		Integer valueToReturn = null;
		Reference reference = (Reference) obj;
		Enumeration<? extends Object> enumeration = reference.getAll();

		while (enumeration.hasMoreElements()) {
			RefAddr refAddr = (RefAddr) enumeration.nextElement();
			String fieldName = refAddr.getType();
			if (fieldName.equals("port")) {
				valueToReturn = Integer.valueOf((String) refAddr.getContent());
			}
		}
		return getRegistry(valueToReturn);
	}

	/**
	 * Locate or create the RMI registry.
	 * 
	 * @param registryPort
	 *            the registry port to use
	 * @return the RMI registry
	 * @throws RemoteException
	 *             if the registry couldn't be located or created
	 */
	protected Registry getRegistry(int registryPort) throws RemoteException {
		if (this.alwaysCreate) {
			return LocateRegistry.createRegistry(registryPort);
		}
		try {
			// Retrieve existing registry.
			Registry reg = LocateRegistry.getRegistry(registryPort);
			testRegistry(reg);
			return reg;
		} catch (RemoteException ex) {
			return LocateRegistry.createRegistry(registryPort);
		}
	}

	/**
	 * Test the given RMI registry, calling some operation on it to check
	 * whether it is still active.
	 * <p>
	 * Default implementation calls <code>Registry.list()</code>.
	 * 
	 * @param registry
	 *            the RMI registry to test
	 * @throws RemoteException
	 *             if thrown by registry methods
	 * @see java.rmi.registry.Registry#list()
	 */
	protected void testRegistry(Registry registry) throws RemoteException {
		registry.list();
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}

}
