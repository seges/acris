package sk.seges.sesam.pap.configuration.processor;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

import sk.seges.sesam.core.configuration.annotation.Configuration;
import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.configuration.configurer.ConfigurationProcessorProviderConfigurer;
import sk.seges.sesam.pap.configuration.model.SettingsTypeElement;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ConfigurationProviderProcessor extends AbstractConfigurableProcessor  {

	public static final String SUFFIX = "Provider";
	
	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new ConfigurationProcessorProviderConfigurer();
	}

	public static NamedType getOutputClass(ImmutableType inputClass) {
		return  inputClass.addClassSufix(SUFFIX);
	}

	@Override
	protected NamedType[] getTargetClassNames(ImmutableType inputClass) {
		return new NamedType[] { getOutputClass(inputClass) };
	}

	@Override
	protected ElementKind getElementKind() {
		return ElementKind.INTERFACE;
	}
	
	@Override
	protected void processElement(TypeElement typeElement, NamedType outputName, RoundEnvironment roundEnv, FormattedPrintWriter pw) {
		Set<? extends Element> classpathElements = getClassPathTypes().getElementsAnnotatedWith(Configuration.class);
		Set<? extends Element> roundElements = roundEnv.getElementsAnnotatedWith(Configuration.class);

		Set<Element> result = new HashSet<Element>();
		
		if (classpathElements != null) {
			for (Element classPathElement: classpathElements) {
				result.add(classPathElement);
			}
		}

		if (roundElements != null) {
			for (Element roundElement: roundElements) {
				boolean found = false;
				for (Element resultElement: result) {
					if (resultElement.toString().equals(roundElement.toString())) {
						found = true;
						break;
					}
				}
				
				if (!found) {
					result.add(roundElement);
				}
			}
		}

		for (Element element: result) {
			NamedType outputClass = new SettingsTypeElement((DeclaredType)element.asType(), processingEnv);
			pw.println(outputClass, " get" + outputClass.getSimpleName() + "();");
			pw.println();
		}
	}
}