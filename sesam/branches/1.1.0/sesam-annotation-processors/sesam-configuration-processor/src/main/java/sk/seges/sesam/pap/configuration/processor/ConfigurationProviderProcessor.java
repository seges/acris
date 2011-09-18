package sk.seges.sesam.pap.configuration.processor;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.pap.configuration.configurer.ConfigurationProcessorProviderConfigurer;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ConfigurationProviderProcessor extends AbstractConfigurationProviderProcessor {

	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new ConfigurationProcessorProviderConfigurer();
	}

}