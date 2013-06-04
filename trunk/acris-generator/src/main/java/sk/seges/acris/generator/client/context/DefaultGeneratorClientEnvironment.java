package sk.seges.acris.generator.client.context;

import sk.seges.acris.generator.client.context.api.GeneratorClientEnvironment;
import sk.seges.acris.generator.client.context.api.TokensCache;

public class DefaultGeneratorClientEnvironment implements GeneratorClientEnvironment {

	private String topLevelDomain;

	private String serverUrl;

	private TokensCache tokensCache;
	
	public DefaultGeneratorClientEnvironment(TokensCache tokensCache) {
		this.tokensCache = tokensCache;
	}
	
	@Override
	public String getTopLevelDomain() {
		return topLevelDomain;
	}

	public void setTopLevelDomain(String topLevelDomain) {
		this.topLevelDomain = topLevelDomain;
	}

	@Override
	public String getServerURL() {
		return serverUrl;
	}

	@Override
	public TokensCache getTokensCache() {
		return tokensCache;
	}

	@Override
	public void setServerURL(String serverUrl) {
		this.serverUrl = serverUrl;
	}	
}