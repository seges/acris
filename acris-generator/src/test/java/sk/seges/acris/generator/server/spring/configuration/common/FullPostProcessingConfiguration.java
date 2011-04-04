package sk.seges.acris.generator.server.spring.configuration.common;

import org.springframework.context.annotation.Bean;

import sk.seges.acris.generator.server.processor.utils.PostProcessorActivator;

public class FullPostProcessingConfiguration {

	@Bean
	public PostProcessorActivator postProcessorActivator() {
		return new PostProcessorActivator("");
	}
}