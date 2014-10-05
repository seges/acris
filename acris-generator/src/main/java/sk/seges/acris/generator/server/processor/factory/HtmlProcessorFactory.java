package sk.seges.acris.generator.server.processor.factory;

import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.HtmlPostProcessor;
import sk.seges.acris.generator.server.processor.factory.api.NodeParserFactory;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.site.server.model.data.WebSettingsData;

import java.util.Collection;

public class HtmlProcessorFactory {

	private Collection<AbstractElementPostProcessor> postProcessors;
	private ContentDataProvider contentMetaDataProvider;
	private PostProcessorActivatorFactory postProcessorActivatorFactory;
	private NodeParserFactory parserFactory;
	
	public HtmlProcessorFactory(Collection<AbstractElementPostProcessor> postProcessors, PostProcessorActivatorFactory postProcessorActivatorFactory, 
			ContentDataProvider contentMetaDataProvider, NodeParserFactory parserFactory) {
		this.postProcessors = postProcessors;
		this.contentMetaDataProvider = contentMetaDataProvider;
		this.postProcessorActivatorFactory = postProcessorActivatorFactory;
		this.parserFactory = parserFactory;
	}
	
	public HtmlPostProcessor create(WebSettingsData webSettings) {
		return new HtmlPostProcessor(postProcessors, postProcessorActivatorFactory.create(webSettings), 
				contentMetaDataProvider, webSettings, parserFactory);
	}
}