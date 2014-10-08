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
	private NodesContext nodesContext;
	private final String defaultLlocale;
	
	public DefaultGeneratorEnvironment(WebSettingsData webSettings, GeneratorToken generatorToken, GeneratorToken defaultToken, ContentData content, 
			boolean indexFile, String defaultLlocale) {
		this.generatorToken = generatorToken;
		this.webSettings = webSettings;
		this.content = content;
		this.indexFile = indexFile;
		this.defaultToken = defaultToken;
		this.defaultLlocale = defaultLlocale;
	}
	
	public String getDefaultLlocale() {
		return defaultLlocale;
	}
	
	@Override
	public NodesContext getNodesContext() {
		return nodesContext;
	}
	
	public void setNodesContext(NodesContext nodesContext) {
		this.nodesContext = nodesContext;
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
		if (defaultToken.isDefaultToken()) {
			return null;
		}
		return defaultToken;
	}
}