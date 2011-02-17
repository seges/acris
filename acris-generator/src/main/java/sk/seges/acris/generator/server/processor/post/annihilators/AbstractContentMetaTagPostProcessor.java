package sk.seges.acris.generator.server.processor.post.annihilators;

import sk.seges.acris.domain.shared.domain.api.ContentData;
import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.acris.site.shared.service.IWebSettingsService;

public abstract class AbstractContentMetaTagPostProcessor extends AbstractMetaTagPostProcessor {

	protected ContentDataProvider contentMetaDataProvider;
	protected ContentData<?> content;

	public AbstractContentMetaTagPostProcessor(IWebSettingsService webSettingsService, ContentDataProvider contentMetaDataProvider) {
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
