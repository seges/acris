package sk.seges.acris.generator.server.processor.mock;

import sk.seges.acris.generator.server.processor.TokenProvider;
import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.acris.showcase.mora.client.configuration.NameTokens;


public class MockTokenProvider implements TokenProvider {

	private GeneratorToken generatorToken;

	public MockTokenProvider() {
		generatorToken = new GeneratorToken();
		GeneratorToken generatorToken = new GeneratorToken();
		generatorToken.setNiceUrl(NameTokens.HOME_PAGE);
		generatorToken.setWebId("generator-showcase");
		generatorToken.setWebId("en");
	}
	
	@Override
	public void setTokenForProcessing(GeneratorToken generatorToken) {
	}

	@Override
	public GeneratorToken getTokenForProcessing() {
		return generatorToken;
	}
}