package sk.seges.acris.generator.server.processor.post.alters;

import sk.seges.acris.domain.shared.domain.api.ContentData;
import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.acris.site.shared.service.IWebSettingsService;

public abstract class AbstractContentMetaDataPostProcessor extends AbstractElementPostProcessor {

	protected ContentDataProvider contentMetaDataProvider;
	protected ContentData<?> content;
	
	protected AbstractContentMetaDataPostProcessor(IWebSettingsService webSettingsService, ContentDataProvider contentMetaDataProvider) {
		super(webSettingsService);
		this.contentMetaDataProvider = contentMetaDataProvider;
	}
	
	public ContentData<?> getContent() {
		return content;
	}
	
	@Override
	public void setGeneratorToken(GeneratorToken token) {
		super.setGeneratorToken(token);
		content = contentMetaDataProvider.getContent(generatorToken);
	}
}
