package sk.seges.acris.test.server.service;

import sk.seges.acris.test.shared.service.CardPayRemoteService;
import sk.seges.corpis.domain.shared.pay.tatra.CardPaySettings;
import sk.seges.corpis.pay.tatra.CardPaySignatureComputer;

public class CardPayService implements CardPayRemoteService {

	private CardPaySettings defaultCardPaySettings;

	public CardPayService(CardPaySettings defaultCardPaySettings) {
		this.defaultCardPaySettings = defaultCardPaySettings;
	}
	
	@Override
	public String computeSign(String variableSymbol, String result, String ac) {
        return new CardPaySignatureComputer().compute(variableSymbol, result, ac, defaultCardPaySettings.getKey());
	}
}