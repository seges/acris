package sk.seges.acris.generator.server.spring.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.ImagesSourcePostProcessor;
import sk.seges.acris.site.shared.service.IWebSettingsService;

@Import({WebSettingsServiceConfiguration.class})
public class ImagePathTestConfiguration {

	@Autowired
	private IWebSettingsService webSettingsService;
	
	@Bean
	public AbstractElementPostProcessor imagesSourcePostProcessor() {
		return new ImagesSourcePostProcessor(webSettingsService);
	}
}