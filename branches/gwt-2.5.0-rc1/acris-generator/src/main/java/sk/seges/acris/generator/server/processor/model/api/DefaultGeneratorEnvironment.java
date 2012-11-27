package sk.seges.acris.generator.server.processor.model.api;

import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.acris.site.server.domain.api.ContentData;
import sk.seges.acris.site.server.model.data.WebSettingsData;

public class DefaultGeneratorEnvironment implements GeneratorEnvironment {

	private GeneratorToken generatorToken;
	private GeneratorToken defaultToken;
	private ContentData content;
	private WebSettingsData webSettings;
	private boolean indexFile;
	
	public DefaultGeneratorEnvironment(WebSettingsData webSettings, GeneratorToken generatorToken, GeneratorToken defaultToken, ContentData content, boolean indexFile) {
		this.generatorToken = generatorToken;
		this.webSettings = webSettings;
		this.content = content;
		this.indexFile = indexFile;
		this.defaultToken = defaultToken;
	}
		
	@Override
	public GeneratorToken getGeneratorToken() {
		return generatorToken;
	}
	
	@Override
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
	
	@Override
	public GeneratorToken getDefaultToken() {
		return defaultToken;
	}
}