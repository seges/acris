/**
 * 
 */
package sk.seges.acris.binding.jsr269;

import java.lang.reflect.Type;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import sk.seges.acris.binding.client.wrappers.BeanWrapper;
import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.configuration.api.OutputDefinition;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.TypedClassBuilder;
import sk.seges.sesam.core.pap.model.api.HasTypeParameters;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.structure.api.PackageValidatorProvider;

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
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class BeanWrapperProcessor extends AbstractConfigurableProcessor {

	public static final String BEAN_WRAPPER_SUFFIX = "BeanWrapper";

	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new BeanWrapperProcessorConfiguration();
	}
	
	public static NamedType getOutputClass(ImmutableType inputClass, PackageValidatorProvider packageValidatorProvider) {
		if (inputClass instanceof HasTypeParameters) {
			inputClass = ((HasTypeParameters)inputClass).stripTypeParameters();
		}
		return inputClass.addClassSufix(BEAN_WRAPPER_SUFFIX);
	}

	@Override
	protected Type[] getOutputDefinition(OutputDefinition type, TypeElement typeElement) {
		switch (type) {
			case OUTPUT_INTERFACES:
				return new Type[] { TypedClassBuilder.get(BeanWrapper.class, genericsSupport.applyUpperGenerics(NamedType.THIS, typeElement)) };
			}
		return super.getOutputDefinition(type, typeElement);
	}

	protected PackageValidatorProvider getPackageValidatorProvider() {
		return new DefaultPackageValidatorProvider();
	}

	@Override
	protected NamedType[] getTargetClassNames(ImmutableType mutableType) {
		return new NamedType[] { getOutputClass(mutableType, getPackageValidatorProvider()) };
	}

	@Override
	protected ElementKind getElementKind() {
		return ElementKind.INTERFACE;
	}
}