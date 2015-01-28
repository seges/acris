package sk.seges.acris.generator.server.spring.configuration.common;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.annotation.Bean;

import sk.seges.acris.generator.server.processor.factory.JSONOfflineWebSettingsFactory;
import sk.seges.acris.generator.server.processor.factory.JSONParameterManagerFactory;
import sk.seges.acris.generator.server.processor.factory.PostProcessorActivatorFactory;
import sk.seges.acris.generator.server.processor.factory.api.OfflineWebSettingsFactory;
import sk.seges.acris.generator.server.processor.factory.api.ParametersManagerFactory;

public class OfflineSettingsConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

	@Bean
	public ParametersManagerFactory parametersManagerFactory(ObjectMapper objectMapper) {
		return new JSONParameterManagerFactory(objectMapper);
	}
	
	@Bean
	public OfflineWebSettingsFactory offlineWebSettingsFactory(ParametersManagerFactory parametersManagerFactory) {
		return new JSONOfflineWebSettingsFactory(parametersManagerFactory);
	}
	
	@Bean
	public PostProcessorActivatorFactory postProcessorActivatorFactory(OfflineWebSettingsFactory offlineWebSettingsFactory) {
		return new PostProcessorActivatorFactory(offlineWebSettingsFactory);
	}
}