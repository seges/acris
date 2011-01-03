/**
 * 
 */
package sk.seges.sesam.model.metadata.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import sk.seges.sesam.model.metadata.annotation.strategy.PojoPropertyConverter;
import sk.seges.sesam.model.metadata.annotation.strategy.api.ModelPropertyConverter;

/**
 * @author eldzi
 * @author Peter Simun (simun@seges.sk)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MetaModel {

	//Do not rename the name, see BeanWrapperProcessor for the reference of that
	Class<? extends ModelPropertyConverter>[] beanPropertyConverter() default {PojoPropertyConverter.class};
}
