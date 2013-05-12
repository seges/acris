package sk.seges.acris.generator.server.spring.configuration.common;

import org.springframework.context.annotation.Bean;

import sk.seges.acris.generator.server.processor.factory.JSONOfflineWebSettingsFactory;
import sk.seges.acris.generator.server.processor.factory.JSONParameterManagerFactory;
import sk.seges.acris.generator.server.processor.factory.PostProcessorActivatorFactory;
import sk.seges.acris.generator.server.processor.factory.api.OfflineWebSettingsFactory;
import sk.seges.acris.generator.server.processor.factory.api.ParametersManagerFactory;

public class JSONOfflineSettingsConfiguration {

	@Bean
	public ParametersManagerFactory parametersManagerFactory() {
		return new JSONParameterManagerFactory();
	}

	@Bean
	public OfflineWebSettingsFactory offlineWebSettingsFactory() {
		return new JSONOfflineWebSettingsFactory(parametersManagerFactory());
	}

	@Bean
	public PostProcessorActivatorFactory postProcessorActivatorFactory() {
		return new PostProcessorActivatorFactory(offlineWebSettingsFactory());
	}

}
