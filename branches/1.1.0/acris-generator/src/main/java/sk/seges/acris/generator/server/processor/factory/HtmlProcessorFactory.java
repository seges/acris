package sk.seges.acris.generator.server.processor.factory;

import java.util.Collection;

import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.HtmlPostProcessing;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.utils.PostProcessorActivator;
import sk.seges.acris.site.shared.domain.api.WebSettingsData;

public class HtmlProcessorFactory {

	private Collection<AbstractElementPostProcessor> postProcessors;
	private ContentDataProvider contentMetaDataProvider;
	private PostProcessorActivator postProcessorActivator;
	
	public HtmlProcessorFactory(Collection<AbstractElementPostProcessor> postProcessors, PostProcessorActivator postProcessorActivator, ContentDataProvider contentMetaDataProvider) {
		this.postProcessors = postProcessors;
		this.contentMetaDataProvider = contentMetaDataProvider;
		this.postProcessorActivator = postProcessorActivator;
	}
	
	public HtmlPostProcessing create(WebSettingsData webSettings) {
		return new HtmlPostProcessing(postProcessors, postProcessorActivator, contentMetaDataProvider, webSettings);
	}
}
