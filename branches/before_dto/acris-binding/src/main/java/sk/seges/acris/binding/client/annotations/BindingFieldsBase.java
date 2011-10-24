package sk.seges.acris.binding.client.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.gwt.beansbinding.core.client.AutoBinding.UpdateStrategy;

import sk.seges.acris.binding.client.holder.validation.ValidationHighligther;

/**
 * Specify strategies associated with all bindings defined in UI form.
 * <p>Annotation is used for define: 
 * <ul>
 * <li><b>{@link UpdateStrategy}</b> - define update strategy for property binding. For more details
 * see {@link UpdateStrateg}. Default strategy is <code>UpdateStrategy.READ</code> and it means, that 
 * binding cannot change original values in bean - only widget values are synchronized
 * regarding the values in the bean<br/><br/></li>
 * <li><b>{@link ValidationStrategy}</b> - define validation strategy for binding process. For
 * more details see {@link ValidationStrategy}. Default strategy is <code>ValidationStrategy.NEVER</code>
 * and it means, that bound values are never validated.</li>
 * </ul>
 * </p>
 */
@Target({ElementType.TYPE})
@Retention(RUNTIME)
public @interface BindingFieldsBase {
	public ValidationStrategy validationStrategy() default ValidationStrategy.NEVER;
	@SuppressWarnings("unchecked")
	Class<? extends ValidationHighligther> validationHighlighter() default ValidationHighligther.class;
	public UpdateStrategy updateStrategy() default UpdateStrategy.READ;
}