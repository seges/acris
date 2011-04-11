package sk.seges.acris.generator.server.spring.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.DescriptionMetaTagPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.ImagesSourcePostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.KeywordsMetaTagPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.LanguageSelectorPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.MetaTagPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.NiceURLLinkPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.ScriptsPathPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.StylesPathPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.TitlePostProcessor;
import sk.seges.acris.generator.server.processor.post.annihilators.DescriptionPostProcessor;
import sk.seges.acris.generator.server.processor.post.annihilators.HeadStyleScriptPostProcessor;
import sk.seges.acris.generator.server.processor.post.annihilators.KeywordsPostProcessor;
import sk.seges.acris.generator.server.processor.post.annihilators.NochacheScriptPostProcessor;
import sk.seges.acris.generator.server.processor.post.annihilators.NotVisibleTagsPostProcessor;
import sk.seges.acris.generator.server.processor.post.annihilators.OnLoadErrorFnPostProcessor;
import sk.seges.acris.generator.server.processor.post.annihilators.OnPropertyErrorFnPostProcessor;
import sk.seges.acris.generator.server.processor.post.annihilators.PropertiesScriptPostProcessor;
import sk.seges.acris.generator.server.processor.post.appenders.DescriptionMetaTagAppenderPostProcessor;
import sk.seges.acris.generator.server.processor.post.appenders.GoogleAnalyticAppenderPostProcessor;
import sk.seges.acris.generator.server.processor.post.appenders.KeywordsMetaTagAppenderPostProcessor;
import sk.seges.acris.generator.server.processor.post.appenders.LocaleGwtPropertyAppenderPostProcessor;
import sk.seges.acris.generator.server.processor.post.appenders.MetaTagAppenderPostProcessor;

public class PostProcessorsConfiguration {

	@Autowired
	private ContentDataProvider contentMetaDataProvider;
	
	@Bean
	public AbstractElementPostProcessor descriptionMetaTagPostProcessor() {
		return new DescriptionMetaTagPostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor imagesSourcePostProcessor() {
		return new ImagesSourcePostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor keywordsMetaTagPostProcessor() {
		return new KeywordsMetaTagPostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor languageSelectorPostProcessor() {
		return new LanguageSelectorPostProcessor(contentMetaDataProvider);
	}

	@Bean
	public AbstractElementPostProcessor niceURLLinkPostProcessor() {
		return new NiceURLLinkPostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor scriptsPathPostProcessor() {
		return new ScriptsPathPostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor stylesPathPostProcessor() {
		return new StylesPathPostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor titlePostProcessor() {
		return new TitlePostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor headStyleScriptPostProcessor() {
		return new HeadStyleScriptPostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor nochacheScriptPostProcessor() {
		return new NochacheScriptPostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor notVisibleTagsPostProcessor() {
		return new NotVisibleTagsPostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor onLoadErrorFnPostProcessor() {
		return new OnLoadErrorFnPostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor onPropertyErrorFnPostProcessor() {
		return new OnPropertyErrorFnPostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor propertiesScriptPostProcessor() {
		return new PropertiesScriptPostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor googleAnalyticPostProcessor() {
		return new GoogleAnalyticAppenderPostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor localeGwtPropertyPostProcessor() {
		return new LocaleGwtPropertyAppenderPostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor metaTagPostProcessor() {
		return new MetaTagPostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor keywordsPostProcessorAnihilator() {
		return new KeywordsPostProcessor();
	}
	
	@Bean
	public AbstractElementPostProcessor metaTagAppenderPostProcessor() {
		return new MetaTagAppenderPostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor keywordsMetaTagAppenderPostProcessor() {
		return new KeywordsMetaTagAppenderPostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor descriptionMetaTagAppenderPostProcessor() {
		return new DescriptionMetaTagAppenderPostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor descriptionMetaTagAnnihilatorPostProcessor() {
		return new DescriptionPostProcessor();
	}
}