package sk.seges.acris.security.server.utils;

import sk.seges.sesam.domain.IDomainObject;

public class SecuredClassHelper {

	@SuppressWarnings("unchecked")
	public static Class<? extends IDomainObject<?>> getSecuredClass(String className) {
		Class<? extends IDomainObject<?>> clazz;
		try {
			clazz = (Class<? extends IDomainObject<?>>) Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Class " + className + " could not be found.");
		}
		return clazz;
	}

}
