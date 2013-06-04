package sk.seges.acris.generator.client.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import sk.seges.acris.generator.client.context.api.TokensCache;
import sk.seges.acris.generator.shared.domain.GeneratorToken;

import com.allen_sauer.gwt.log.client.Log;

public class MapTokenCache implements TokensCache {

	private GeneratorToken generatorToken;
	private GeneratorToken defaultToken;

	private List<String> awaitingTokens = new ArrayList<String>();
	private Set<String> processedTokens = new HashSet<String>();
	
	@Override
	public int getTokensCount() {
		return awaitingTokens.size() + processedTokens.size();
	}

	public void remove() {
		throw new RuntimeException("Remove operation is not supported!");
	}

	public boolean hasNext() {
		return awaitingTokens.size() > 0;
	}

	public GeneratorToken next() {
		if (!hasNext()) {
			throw new RuntimeException("There are no more tokens in the cache!");
		}

		String niceurl = awaitingTokens.remove(0);
		processedTokens.add(niceurl);

		generatorToken = new GeneratorToken();

		generatorToken.setNiceUrl(niceurl);
		generatorToken.setWebId(defaultToken.getWebId());
		generatorToken.setLanguage(defaultToken.getLanguage());
		generatorToken.setAlias(defaultToken.getAlias());
		
		if (generatorToken.getNiceUrl().equals(defaultToken.getNiceUrl())) {
			generatorToken.setDefaultToken(true);
		}
		
		return generatorToken;
	}

	protected boolean isAlreadyRegitered(String token, Iterator<String> iterator) {
		while (iterator.hasNext()) {
			if (iterator.next().equals(token)) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean isAlreadyAwaiting(String token) {
		return isAlreadyRegitered(token, awaitingTokens.iterator());
	}
	
	protected boolean isAlreadyProcessed(String token) {
		return isAlreadyRegitered(token, processedTokens.iterator());		
	}
	
	@Override
	public void addTokens(Collection<String> tokens) {
		Iterator<String> iterator = tokens.iterator();
		
		while (iterator.hasNext()) {
			String token = iterator.next();

			if (!isAlreadyProcessed(token) && !isAlreadyAwaiting(token)) {
				Log.info("Adding token for a processing " + token);
				if (token == null || token.length() == 0) {
					if (defaultToken == null) {
						awaitingTokens.add("");
					}
				} else {
					awaitingTokens.add(token);
				}
			}
		}
	}
	
	@Override
	public GeneratorToken getCurrentToken() {
		return generatorToken;
	}

	@Override
	public void setDefaultToken(GeneratorToken generatorToken) {
		this.defaultToken = generatorToken;
	}

	@Override
	public GeneratorToken getDefaultToken() {
		return defaultToken;
	}

	@Override
	public int getWaitingTokensCount() {
		return awaitingTokens.size();
	}
}