package sk.seges.acris.generator.server.spring.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

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
import sk.seges.acris.generator.server.rewriterules.INiceUrlGenerator;
import sk.seges.acris.generator.server.rewriterules.SunConditionalNiceURLGenerator;
import sk.seges.acris.site.shared.service.IWebSettingsService;

public class PostProcessorsConfiguration {

	@Autowired
	@Qualifier("url.redirect.single.file")
	protected Boolean redirectSingleFile;

	@Autowired
	@Qualifier("url.redirect.condition")
	protected Boolean redirectCondition;

	@Autowired
	@Qualifier("url.redirect.file.location")
	private String redirectFilePath;

	@Autowired
	@Qualifier("legacy.url.redirect.single.file")
	private Boolean legacyRedirectSingleFile;

	@Autowired
	@Qualifier("legacy.url.redirect.file.location")
	private String legacyRedirectFilePath;

	@Autowired
	private IWebSettingsService webSettingsService;

	@Autowired
	private ContentDataProvider contentMetaDataProvider;
	
	@Bean
	@Scope("prototype")
	public INiceUrlGenerator sunConditionalURLGenerator() {
		return new SunConditionalNiceURLGenerator(redirectFilePath, redirectCondition, redirectSingleFile, legacyRedirectFilePath, legacyRedirectSingleFile);
	}

	@Bean
	public AbstractElementPostProcessor descriptionMetaTagPostProcessor() {
		return new DescriptionMetaTagPostProcessor(webSettingsService, contentMetaDataProvider);
	}

	@Bean
	public AbstractElementPostProcessor imagesSourcePostProcessor() {
		return new ImagesSourcePostProcessor(webSettingsService);
	}

	@Bean
	public AbstractElementPostProcessor keywordsMetaTagPostProcessor() {
		return new KeywordsMetaTagPostProcessor(webSettingsService, contentMetaDataProvider);
	}

	@Bean
	public AbstractElementPostProcessor languageSelectorPostProcessor() {
		return new LanguageSelectorPostProcessor(webSettingsService, contentMetaDataProvider);
	}

	@Bean
	public AbstractElementPostProcessor niceURLLinkPostProcessor() {
		return new NiceURLLinkPostProcessor(webSettingsService);
	}

	@Bean
	public AbstractElementPostProcessor scriptsPathPostProcessor() {
		return new ScriptsPathPostProcessor(webSettingsService);
	}

	@Bean
	public AbstractElementPostProcessor stylesPathPostProcessor() {
		return new StylesPathPostProcessor(webSettingsService);
	}

	@Bean
	public AbstractElementPostProcessor titlePostProcessor() {
		return new TitlePostProcessor(webSettingsService, contentMetaDataProvider);
	}

	@Bean
	public AbstractElementPostProcessor headStyleScriptPostProcessor() {
		return new HeadStyleScriptPostProcessor(webSettingsService);
	}

	@Bean
	public AbstractElementPostProcessor nochacheScriptPostProcessor() {
		return new NochacheScriptPostProcessor(webSettingsService);
	}

	@Bean
	public AbstractElementPostProcessor notVisibleTagsPostProcessor() {
		return new NotVisibleTagsPostProcessor(webSettingsService);
	}

	@Bean
	public AbstractElementPostProcessor onLoadErrorFnPostProcessor() {
		return new OnLoadErrorFnPostProcessor(webSettingsService);
	}

	@Bean
	public AbstractElementPostProcessor onPropertyErrorFnPostProcessor() {
		return new OnPropertyErrorFnPostProcessor(webSettingsService);
	}

	@Bean
	public AbstractElementPostProcessor propertiesScriptPostProcessor() {
		return new PropertiesScriptPostProcessor(webSettingsService);
	}

	@Bean
	public AbstractElementPostProcessor googleAnalyticPostProcessor() {
		return new GoogleAnalyticAppenderPostProcessor(webSettingsService);
	}

	@Bean
	public AbstractElementPostProcessor localeGwtPropertyPostProcessor() {
		return new LocaleGwtPropertyAppenderPostProcessor(webSettingsService);
	}

	@Bean
	public AbstractElementPostProcessor metaTagPostProcessor() {
		return new MetaTagPostProcessor(webSettingsService, contentMetaDataProvider);
	}

	@Bean
	public AbstractElementPostProcessor keywordsPostProcessorAnihilator() {
		return new KeywordsPostProcessor(webSettingsService, contentMetaDataProvider);
	}
	
	@Bean
	public AbstractElementPostProcessor metaTagAppenderPostProcessor() {
		return new MetaTagAppenderPostProcessor(webSettingsService, contentMetaDataProvider);
	}

	@Bean
	public AbstractElementPostProcessor keywordsMetaTagAppenderPostProcessor() {
		return new KeywordsMetaTagAppenderPostProcessor(webSettingsService, contentMetaDataProvider);
	}

	@Bean
	public AbstractElementPostProcessor descriptionMetaTagAppenderPostProcessor() {
		return new DescriptionMetaTagAppenderPostProcessor(webSettingsService, contentMetaDataProvider);
	}

	@Bean
	public AbstractElementPostProcessor descriptionMetaTagAnnihilatorPostProcessor() {
		return new DescriptionPostProcessor(webSettingsService, contentMetaDataProvider);
	}

//	@Bean
//	public HtmlPostProcessing htmlPostProcessing() {
//		Map<String, AbstractElementPostProcessor> abstractPostProcessors = this.applicationContext.getBeansOfType(AbstractElementPostProcessor.class);
//		return new HtmlPostProcessing(abstractPostProcessors.values());
//	}
}