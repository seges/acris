package sk.seges.acris.generator.server.processor.factory;

import java.util.Collection;

import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.HtmlPostProcessing;
import sk.seges.acris.generator.server.processor.factory.api.ParserFactory;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.utils.PostProcessorActivator;
import sk.seges.acris.site.shared.domain.api.WebSettingsData;

public class HtmlProcessorFactory {

	private Collection<AbstractElementPostProcessor> postProcessors;
	private ContentDataProvider contentMetaDataProvider;
	private PostProcessorActivator postProcessorActivator;
	private ParserFactory parserFactory;
	
	public HtmlProcessorFactory(Collection<AbstractElementPostProcessor> postProcessors, PostProcessorActivator postProcessorActivator, 
			ContentDataProvider contentMetaDataProvider, ParserFactory parserFactory) {
		this.postProcessors = postProcessors;
		this.contentMetaDataProvider = contentMetaDataProvider;
		this.postProcessorActivator = postProcessorActivator;
		this.parserFactory = parserFactory;
	}
	
	public HtmlPostProcessing create(WebSettingsData webSettings) {
		return new HtmlPostProcessing(postProcessors, postProcessorActivator, contentMetaDataProvider, webSettings, parserFactory);
	}
}
