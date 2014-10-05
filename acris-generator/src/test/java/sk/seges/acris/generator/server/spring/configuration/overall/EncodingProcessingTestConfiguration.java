package sk.seges.acris.generator.server.spring.configuration.overall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.*;
import sk.seges.acris.generator.server.spring.configuration.common.MockTestConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.OfflineSettingsConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.WebSettingsServiceConfiguration;

@Import({WebSettingsServiceConfiguration.class, MockTestConfiguration.class, OfflineSettingsConfiguration.class})
public class EncodingProcessingTestConfiguration {
	
	@Autowired
	private ContentDataProvider contentMetaDataProvider;
	
	@Bean
	public AbstractElementPostProcessor imagesSourcePostProcessor() {
		return new ImagesSourceAlterPostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor imageLanguageSelectorAlterPostProcessor() {
		return new ImageLanguageSelectorAlterPostProcessor(contentMetaDataProvider);
	}

	@Bean
	public AbstractElementPostProcessor linkLanguageSelectorAlterPostProcessor() {
		return new LinkLanguageSelectorAlterPostProcessor(contentMetaDataProvider);
	}

	@Bean
	public AbstractElementPostProcessor selectLanguageSelectorAlterPostProcessor() {
		return new SelectLanguageSelectorAlterPostProcessor(contentMetaDataProvider);
	}
	
	@Bean
	public AbstractElementPostProcessor imageGalleryPostProcessor() {
		return new ImageGalleryPathAlterPostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor niceURLLinkPostProcessor() {
		return new NiceURLLinkAlterPostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor scriptsPathPostProcessor() {
		return new ScriptsAlterPathPostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor metaTagPostProcessor() {
		return new MetaTagAlterPostProcessor();
	}
	
	@Bean
	public AbstractElementPostProcessor stylesAlterPathPostProcessor() {
		return new StylesAlterPathPostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor descriptionAlterMetaTagPostProcessor() {
		return new DescriptionMetaTagAlterPostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor keywordsAltersMetaTagPostProcessor() {
		return new KeywordsMetaTagAlterPostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor titleAlterPostProcessor() {
		return new TitleAlterPostProcessor();
	}
}