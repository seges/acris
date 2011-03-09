package sk.seges.acris.generator.server.processor.factory;

import java.util.Collection;

import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.HtmlPostProcessing;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.site.shared.domain.api.WebSettingsData;

public class HtmlProcessorFactory {

	private Collection<AbstractElementPostProcessor> postProcessors;
	private ContentDataProvider contentMetaDataProvider;
	
	public HtmlProcessorFactory(Collection<AbstractElementPostProcessor> postProcessors, ContentDataProvider contentMetaDataProvider) {
		this.postProcessors = postProcessors;
		this.contentMetaDataProvider = contentMetaDataProvider;
	}
	
	public HtmlPostProcessing create(WebSettingsData webSettings) {
		return new HtmlPostProcessing(postProcessors, contentMetaDataProvider, webSettings);
	}
}
