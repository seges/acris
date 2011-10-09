package sk.seges.acris.binding.client.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
/**
 * <p>Specifies data loader for specific data. Using this annotation you can specify own
 * data loader which provides data to the binding widget. Data loader should implement
 * {@see IAsyncDataLoader} interface and load load method to provide data for specific
 * {@see Page}.</p>
 * <p>DataLoader should have empty constructor because it is instanciated using
 * non-argument constructor.</p>
 */
public @interface BindingSpecLoader {
	abstract Class<?> value();
}