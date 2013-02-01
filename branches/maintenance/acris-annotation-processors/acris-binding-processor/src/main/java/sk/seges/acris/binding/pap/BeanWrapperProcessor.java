/**
 * 
 */
package sk.seges.acris.binding.pap;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import sk.seges.acris.binding.pap.configurer.BeanWrapperProcessorConfigurer;
import sk.seges.acris.binding.pap.model.BeanWrapperType;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.processor.MutableAnnotationProcessor;

/**
 * Generates bean wrapper interfaces for all relevant classes. The definition of which classes to process is following
 * the rule:
 * <ul>
 * <li>by default {@link sk.seges.acris.binding.client.annotations.BeanWrapper} annotated classes are taken</li>
 * <li>addition configuration is read from project's META-INF/bean-wrapper.properties file</li>
 * </ul>
 * 
 * @author eldzi
 * @author Peter Simun (simun@seges.sk)
 */
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class BeanWrapperProcessor extends MutableAnnotationProcessor {

	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new BeanWrapperProcessorConfigurer();
	}
	
	@Override
	protected boolean checkPreconditions(ProcessorContext context, boolean alreadyExists) {
		return !alreadyExists;
	}

	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] {
			new BeanWrapperType(context.getMutableType(), processingEnv)
		};
	}
	
	@Override
	protected void processElement(ProcessorContext context) {}
}