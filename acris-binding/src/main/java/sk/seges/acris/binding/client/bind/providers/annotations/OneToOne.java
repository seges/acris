package sk.seges.acris.binding.client.bind.providers.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import sk.seges.acris.binding.client.bind.providers.support.AbstractBindingChangeHandlerAdapterProvider;

@Target({ ElementType.TYPE })
@Retention(RUNTIME)
public @interface OneToOne {
	public String value() default AbstractBindingChangeHandlerAdapterProvider.PROPERTY_VALUE;
}
