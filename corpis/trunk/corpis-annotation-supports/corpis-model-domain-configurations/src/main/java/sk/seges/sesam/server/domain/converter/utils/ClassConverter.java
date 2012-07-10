package sk.seges.sesam.server.domain.converter.utils;

import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

public class ClassConverter {

	public static String getDomainClassName(String dtoClassName) {
		if (dtoClassName == null) {
			return dtoClassName;
		}
		
		Class<?> dtoClass = null;
		try {
			dtoClass = Class.forName(dtoClassName);
		} catch (ClassNotFoundException e) {
			return dtoClassName;
		}
		
		TransferObjectMapping annotation = dtoClass.getAnnotation(TransferObjectMapping.class);
		
		if (annotation == null) {
			return dtoClassName;
		}
		
		return annotation.domainClassName();
	}
}
