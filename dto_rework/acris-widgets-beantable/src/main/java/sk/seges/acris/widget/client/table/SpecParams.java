package sk.seges.acris.widget.client.table;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SpecParams {
	/**
	 * Instruct generator to create only specification implementation not the
	 * whole table.
	 */
	boolean onlySpec() default false;

	/**
	 * Property name of a field marked with @Id annotation in the bean. If its
	 * value is null, no ID field will be preserved in projectables.
	 */
	String idProperty() default "id";
}
