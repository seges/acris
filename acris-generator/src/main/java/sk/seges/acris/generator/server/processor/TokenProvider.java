package sk.seges.acris.generator.server.processor;

import sk.seges.acris.generator.shared.domain.GeneratorToken;

public interface TokenProvider {

	void setTokenForProcessing(final GeneratorToken generatorToken);

	GeneratorToken getTokenForProcessing();
}