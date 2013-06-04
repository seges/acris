package sk.seges.acris.generator.client.context.api;

public interface GeneratorClientEnvironment {

	String getTopLevelDomain();

	void setTopLevelDomain(String topLevelDomain);
	
	String getServerURL();

	void setServerURL(String serverUrl);

	TokensCache getTokensCache();
}