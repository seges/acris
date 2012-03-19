package sk.seges.sesam.pap.service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface LocalServiceDefinition {
	
	/**
	 * Do not change the name unless change it in the {@link ServiceInterfaceProcessor}
	 */
	Class<?> remoteService();
}