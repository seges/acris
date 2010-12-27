package sk.seges.sesam.jndi.rmi;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

/**
 * port for RMI can be set via jndi (<Resource auth="Container"
 * name="rmi/reporting" factory="sk.seges.sesam.jndi.rmi.RmiPortProvider"
 * type="java.lang.Integer" port="11991" />)
 * 
 * @author marta
 * 
 */
public class RmiPortProvider implements ObjectFactory {

	private int port = 1199;

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
		// return LocateRegistry.createRegistry(valueToReturn);

		return valueToReturn;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}

}
