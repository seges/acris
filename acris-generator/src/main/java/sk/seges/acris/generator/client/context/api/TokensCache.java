package sk.seges.acris.generator.client.context.api;

import sk.seges.acris.generator.shared.domain.GeneratorToken;

import java.util.Collection;
import java.util.Iterator;

public interface TokensCache extends Iterator<GeneratorToken> {

	int getWaitingTokensCount();

	int getTokensCount();

	void addTokens(Collection<String> tokens);

	GeneratorToken getCurrentToken();

	void setDefaultToken(GeneratorToken generatorToken);

	GeneratorToken getDefaultToken();

	void setDefaultLocale(String locale);
}