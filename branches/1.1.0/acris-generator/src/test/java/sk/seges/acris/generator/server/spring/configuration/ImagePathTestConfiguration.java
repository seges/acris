package sk.seges.acris.generator.server.spring.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.ImagesSourcePostProcessor;
import sk.seges.acris.generator.server.spring.configuration.common.MockTestConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.WebSettingsServiceConfiguration;

@Import({WebSettingsServiceConfiguration.class, MockTestConfiguration.class})
public class ImagePathTestConfiguration {

	@Bean
	public AbstractElementPostProcessor imagesSourcePostProcessor() {
		return new ImagesSourcePostProcessor();
	}
}