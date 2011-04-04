package sk.seges.acris.generator.server.spring.configuration.common;

import org.springframework.context.annotation.Bean;

import sk.seges.acris.generator.server.processor.post.annihilators.NochacheScriptPostProcessor;
import sk.seges.acris.generator.server.processor.utils.PostProcessorActivator;

public class NoScriptProcessingDisabled {

	@Bean
	public PostProcessorActivator postProcessorActivator() {
		return new PostProcessorActivator(NochacheScriptPostProcessor.class.getSimpleName());
	}
}