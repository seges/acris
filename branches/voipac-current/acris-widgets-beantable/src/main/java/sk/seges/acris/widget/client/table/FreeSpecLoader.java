package sk.seges.acris.widget.client.table;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FreeSpecLoader {
	/**
	 * @return Type of the service used by loader - interface(not Async)
	 */
	Class<?> serviceClass();
	
	String serviceEntryPoint();

	/**
	 * It allows to define what service method must be called to load records to
	 * the table. The method must have two parameters - Page and AsyncCallback.
	 * 
	 * By default <i>findAll</i> is used to get all records.
	 */
	String loaderMethodName() default "findAll";
}
