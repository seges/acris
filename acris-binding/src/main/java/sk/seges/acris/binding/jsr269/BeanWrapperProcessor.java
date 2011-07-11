/**
 * 
 */
package sk.seges.acris.binding.jsr269;

import java.io.PrintWriter;
import java.lang.reflect.Type;

import javax.annotation.Generated;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

import sk.seges.acris.binding.client.wrappers.BeanWrapper;
import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.model.TypedClassBuilder;
import sk.seges.sesam.core.pap.model.api.HasTypeParameters;
import sk.seges.sesam.core.pap.model.api.MutableType;
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
@SupportedOptions({ BeanWrapperProcessor.CONFIG_FILE_LOCATION })
public class BeanWrapperProcessor extends AbstractConfigurableProcessor {

	private static final String DEFAULT_CONFIG_FILE_LOCATION = "/META-INF/bean-wrapper.properties";

	public static final String BEAN_WRAPPER_SUFFIX = "BeanWrapper";

	public static NamedType getOutputClass(MutableType inputClass, PackageValidatorProvider packageValidatorProvider) {
		if (inputClass instanceof HasTypeParameters) {
			inputClass = ((HasTypeParameters)inputClass).stripTypeParameters();
		}
		return inputClass.addClassSufix(BEAN_WRAPPER_SUFFIX);
	}

	@Override
	protected Type[] getConfigurationTypes(DefaultConfigurationType type, TypeElement typeElement) {
		switch (type) {
		case PROCESSING_ANNOTATIONS:
			return new Type[] { sk.seges.acris.binding.client.annotations.BeanWrapper.class };
		case OUTPUT_INTERFACES:
			return new Type[] { TypedClassBuilder.get(BeanWrapper.class, genericsSupport.applyUpperGenerics(NamedType.THIS, typeElement)) };
		}
		return super.getConfigurationTypes(type, typeElement);
	}

	protected PackageValidatorProvider getPackageValidatorProvider() {
		return new DefaultPackageValidatorProvider();
	}

	@Override
	protected boolean processElement(Element element, RoundEnvironment roundEnv) {
		processingEnv.getMessager().printMessage(Kind.NOTE, "Processing " + element.getSimpleName().toString());
		return super.processElement(element, roundEnv);
	}

	@Override
	protected NamedType[] getTargetClassNames(MutableType mutableType) {
		return new NamedType[] { getOutputClass(mutableType, getPackageValidatorProvider()) };
	}

	@Override
	protected ElementKind getElementKind() {
		return ElementKind.INTERFACE;
	}

	@Override
	protected String getConfigurationFileLocation() {
		return DEFAULT_CONFIG_FILE_LOCATION;
	}

	@Override
	protected void writeClassAnnotations(PrintWriter pw, Element el) {
		pw.println("@" + Generated.class.getCanonicalName() + "(\"" + BeanWrapperProcessor.class.getCanonicalName()
				+ "\")");
	}
}