package sk.seges.acris.generator.server.spring.configuration.overall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.DescriptionMetaTagAlterPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.ImagesSourceAlterPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.KeywordsMetaTagAlterPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.LanguageSelectorAlterPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.LocaleGwtPropertyAlterPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.MetaTagAlterPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.NiceURLLinkAlterPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.ScriptsAlterPathPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.StylesAlterPathPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.TitleAlterPostProcessor;
import sk.seges.acris.generator.server.processor.post.annihilators.DescriptionAnnihilatorPostProcessor;
import sk.seges.acris.generator.server.processor.post.annihilators.HeadStyleScriptAnnihilatorPostProcessor;
import sk.seges.acris.generator.server.processor.post.annihilators.KeywordsMetaTagAnnihilatorPostProcessor;
import sk.seges.acris.generator.server.processor.post.annihilators.NochacheScriptAnnihilatorPostProcessor;
import sk.seges.acris.generator.server.processor.post.annihilators.NotVisibleTagsAnnihilatorPostProcessor;
import sk.seges.acris.generator.server.processor.post.annihilators.OnLoadErrorFnAnnihilatorPostProcessor;
import sk.seges.acris.generator.server.processor.post.annihilators.OnPropertyErrorFnAnnihilatorPostProcessor;
import sk.seges.acris.generator.server.processor.post.annihilators.PropertiesScriptAnnihilatorPostProcessor;
import sk.seges.acris.generator.server.processor.post.annihilators.TitleAnnihilatorPostProcessor;
import sk.seges.acris.generator.server.processor.post.appenders.GoogleAnalyticAppenderPostProcessor;
import sk.seges.acris.generator.server.spring.configuration.common.MockTestConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.OfflineSettingsConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.WebSettingsServiceConfiguration;

@Import({WebSettingsServiceConfiguration.class, MockTestConfiguration.class, 
	OfflineSettingsConfiguration.class})
public class DefaultPostprocessorAlterTestConfiguration {

	@Autowired
	private ContentDataProvider contentMetaDataProvider;
	
//	@Bean
//	public AbstractElementPostProcessor imagesSourcePostProcessor() {
//		return new ImagesSourceAlterPostProcessor();
//	}
//
//	@Bean
//	public AbstractElementPostProcessor languageSelectorPostProcessor() {
//		return new LanguageSelectorAlterPostProcessor(contentMetaDataProvider);
//	}
//
//	@Bean
//	public AbstractElementPostProcessor niceURLLinkPostProcessor() {
//		return new NiceURLLinkAlterPostProcessor();
//	}
//
//	@Bean
//	public AbstractElementPostProcessor scriptsPathPostProcessor() {
//		return new ScriptsAlterPathPostProcessor();
//	}
//
//	@Bean
//	public AbstractElementPostProcessor headStyleScriptPostProcessor() {
//		return new HeadStyleScriptAnnihilatorPostProcessor();
//	}
//
//	@Bean
//	public AbstractElementPostProcessor nochacheScriptPostProcessor() {
//		return new NochacheScriptAnnihilatorPostProcessor();
//	}
//
//	@Bean
//	public AbstractElementPostProcessor notVisibleTagsPostProcessor() {
//		return new NotVisibleTagsAnnihilatorPostProcessor();
//	}
//
//	@Bean
//	public AbstractElementPostProcessor onLoadErrorFnPostProcessor() {
//		return new OnLoadErrorFnAnnihilatorPostProcessor();
//	}
//
//	@Bean
//	public AbstractElementPostProcessor onPropertyErrorFnPostProcessor() {
//		return new OnPropertyErrorFnAnnihilatorPostProcessor();
//	}
//
//	@Bean
//	public AbstractElementPostProcessor propertiesScriptPostProcessor() {
//		return new PropertiesScriptAnnihilatorPostProcessor();
//	}
//
//	@Bean
//	public AbstractElementPostProcessor googleAnalyticPostProcessor() {
//		return new GoogleAnalyticAppenderPostProcessor();
//	}
//
//	@Bean
//	public AbstractElementPostProcessor localeGwtPropertyPostProcessor() {
//		return new LocaleGwtPropertyAppenderPostProcessor();
//	}
//
//	@Bean
//	public AbstractElementPostProcessor metaTagPostProcessor() {
//		return new MetaTagAlterPostProcessor();
//	}
//	
//	@Bean
//	public AbstractElementPostProcessor stylesAlterPathPostProcessor() {
//		return new StylesAlterPathPostProcessor();
//	}
//
//	@Bean
//	public AbstractElementPostProcessor descriptionAlterMetaTagPostProcessor() {
//		return new DescriptionMetaTagAlterPostProcessor();
//	}
//
//	@Bean
//	public AbstractElementPostProcessor descriptionMetaTagAnnihilatorPostProcessor() {
//		return new DescriptionAnnihilatorPostProcessor();
//	}
//
//	@Bean
//	public AbstractElementPostProcessor keywordsAltersMetaTagPostProcessor() {
//		return new KeywordsMetaTagAlterPostProcessor();
//	}
//
//	@Bean
//	public AbstractElementPostProcessor keywordsAnihilatorPostProcessor() {
//		return new KeywordsMetaTagAnnihilatorPostProcessor();
//	}
//
//	@Bean
//	public AbstractElementPostProcessor titleAnnihilatorPostProcessor() {
//		return new TitleAnnihilatorPostProcessor();
//	}
//
//	@Bean
//	public AbstractElementPostProcessor titleAlterPostProcessor() {
//		return new TitleAlterPostProcessor();
//	}
}