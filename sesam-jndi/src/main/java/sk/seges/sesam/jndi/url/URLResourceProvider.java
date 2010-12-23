package sk.seges.sesam.jndi.url;

import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

/**
 * 
 * @author marta
 *
 */
public class URLResourceProvider implements ObjectFactory {
	public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment)
			throws Exception {

		URL valueToReturn = null;
		Reference reference = (Reference) obj;

		Enumeration<? extends Object> enumeration = reference.getAll();

		while (enumeration.hasMoreElements()) {
			RefAddr refAddr = (RefAddr) enumeration.nextElement();
			String fieldName = refAddr.getType();
			if (fieldName.equals("specification")) {
				valueToReturn = new URL((String) refAddr.getContent());
			}
		}

		return valueToReturn;
	}

}
