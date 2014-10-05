package sk.seges.acris.generator.server.spring.configuration.common;

import org.springframework.context.annotation.Bean;
import sk.seges.acris.generator.server.processor.factory.CommaSeparatedParameterManagerFactory;
import sk.seges.acris.generator.server.processor.factory.PlainOfflineWebSettingsFactory;
import sk.seges.acris.generator.server.processor.factory.PostProcessorActivatorFactory;
import sk.seges.acris.generator.server.processor.factory.api.OfflineWebSettingsFactory;
import sk.seges.acris.generator.server.processor.factory.api.ParametersManagerFactory;

public class OfflineSettingsConfiguration {

	@Bean
	public ParametersManagerFactory parametersManagerFactory() {
		return new CommaSeparatedParameterManagerFactory();
	}
	
	@Bean
	public OfflineWebSettingsFactory offlineWebSettingsFactory() {
		return new PlainOfflineWebSettingsFactory(parametersManagerFactory());
	}
	
	@Bean
	public PostProcessorActivatorFactory postProcessorActivatorFactory() {
		return new PostProcessorActivatorFactory(offlineWebSettingsFactory());
	}

}
