package sk.seges.sesam.pap.test.selenium.processor;

import java.lang.reflect.Type;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

import sk.seges.sesam.core.pap.configuration.api.OutputDefinition;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.test.selenium.configuration.model.CoreSeleniumSettingsProvider;
import sk.seges.sesam.pap.configuration.processor.AbstractConfigurationProviderProcessor;
import sk.seges.sesam.pap.test.selenium.processor.configurer.SeleniumConfigurationProcessorProviderConfigurer;
import sk.seges.sesam.pap.test.selenium.processor.model.SeleniumSettingsProviderTypeElement;
import sk.seges.sesam.pap.test.selenium.processor.model.SeleniumSuiteTypeElement;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class SeleniumConfigurationProviderProcessor extends AbstractConfigurationProviderProcessor {

	@Override
	protected Type[] getOutputDefinition(OutputDefinition type, TypeElement typeElement) {
		switch (type) {
			case OUTPUT_SUPERCLASS:
				return new Type[] { CoreSeleniumSettingsProvider.class };
		}
		return super.getOutputDefinition(type, typeElement);
	}
	
	@Override
	protected NamedType[] getTargetClassNames(ImmutableType inputClass) {
		return new NamedType[] { 
				new SeleniumSettingsProviderTypeElement(
						new SeleniumSuiteTypeElement((TypeElement)((DeclaredType)inputClass.asType()).asElement(), processingEnv), processingEnv) };
	}

	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new SeleniumConfigurationProcessorProviderConfigurer();
	}	
}