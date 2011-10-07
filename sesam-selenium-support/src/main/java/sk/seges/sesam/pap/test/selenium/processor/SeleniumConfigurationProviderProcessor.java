package sk.seges.sesam.pap.test.selenium.processor;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.pap.configuration.processor.AbstractConfigurationProviderProcessor;
import sk.seges.sesam.pap.test.selenium.processor.configurer.SeleniumSuiteProcessorConfigurer;
import sk.seges.sesam.pap.test.selenium.processor.model.SeleniumSettingsProviderTypeElement;
import sk.seges.sesam.pap.test.selenium.processor.model.SeleniumSuiteTypeElement;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class SeleniumConfigurationProviderProcessor extends AbstractConfigurationProviderProcessor {

	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] { 
				new SeleniumSettingsProviderTypeElement(new SeleniumSuiteTypeElement(context.getTypeElement(), processingEnv), processingEnv) };
	}

	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new SeleniumSuiteProcessorConfigurer();
	}

}