package sk.seges.acris.widget.client.table;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SpecLoader {
	/**
	 * @return Name of a chocolate holding service used by loader.
	 */
	String serviceChocolate();
	
	/**
	 * @return Type of the service used by loader.
	 */
	Class<?> serviceClass();

	/**
	 * It allows to define what service method must be called to load records to
	 * the table. The method must have two parameters - Page and AsyncCallback.
	 * 
	 * By default <i>findAll</i> is used to get all records.
	 */
	String loaderMethodName() default "findAll";
}
