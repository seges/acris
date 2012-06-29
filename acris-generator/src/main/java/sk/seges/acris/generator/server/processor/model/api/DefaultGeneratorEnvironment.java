package sk.seges.acris.generator.server.processor.model.api;

import sk.seges.acris.domain.shared.domain.api.ContentData;
import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.acris.site.shared.domain.api.server.model.data.WebSettingsData;

public class DefaultGeneratorEnvironment implements GeneratorEnvironment {

	private GeneratorToken generatorToken;
	private ContentData content;
	private WebSettingsData webSettings;
	private boolean indexFile;
	
	public DefaultGeneratorEnvironment(WebSettingsData webSettings, GeneratorToken generatorToken, ContentData content, boolean indexFile) {
		this.generatorToken = generatorToken;
		this.webSettings = webSettings;
		this.content = content;
		this.indexFile = indexFile;
	}
		
	public GeneratorToken getGeneratorToken() {
		return generatorToken;
	}
	
	public ContentData getContent() {
		return content;
	}
	
	public WebSettingsData getWebSettings() {
		return webSettings;
	}
	
	@Override
	public boolean isIndexFile() {
		return indexFile;
	}
}