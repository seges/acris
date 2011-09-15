package sk.seges.sesam.pap.configuration.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
		ArrayList<Element> elements = new ArrayList<Element>(getClassPathTypes().getElementsAnnotatedWith(Configuration.class, roundEnv));
		
		Collections.sort(elements, new Comparator<Element>() {

			@Override
			public int compare(Element o1, Element o2) {
				return o1.toString().compareTo(o2.toString());
			}
		});
		
		for (Element element: elements) {
			NamedType outputClass = new SettingsTypeElement((DeclaredType)element.asType(), processingEnv);
			pw.println(outputClass, " get" + outputClass.getSimpleName() + "();");
			pw.println();
		}
	}
}