package sk.seges.acris.security.server.utils;

import sk.seges.sesam.security.shared.domain.ISecuredObject;

public class SecuredClassHelper {

	@SuppressWarnings("unchecked")
	public static Class<? extends ISecuredObject<?>> getSecuredClass(String className) {
		Class<? extends ISecuredObject<?>> clazz;
		try {
			clazz = (Class<? extends ISecuredObject<?>>) Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Class " + className + " could not be found.");
		}
		return clazz;
	}

}
