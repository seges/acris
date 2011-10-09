package sk.seges.sesam.core.annotation.configuration;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.annotation.processing.AbstractProcessor;

/**
 * Annotation indicates that java class configures the specific annotation
 * processor. You should explicitly configure which classes has to be processed
 * or you can declare interfaces that implementors will be processed by annotation
 * processor. 
 * 
 * Although the configuration class can be annotated by any annotation, it won't 
 * be processed by annotation processor. It is used just a processor starter.
 * 
 * @author Peter Simun (sinmun@seges.sk)
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface ProcessorConfiguration {
	
	Class<? extends AbstractProcessor> processor();

}
