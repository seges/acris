package sk.seges.acris.test.client.cardpay.activity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import sk.seges.acris.test.client.cardpay.display.CardPayDisplay;
import sk.seges.acris.test.client.mvp.factory.ClientFactory;
import sk.seges.acris.test.shared.service.CardPayRemoteServiceAsync;
import sk.seges.corpis.domain.pay.tatra.CardPayRequest;
import sk.seges.corpis.domain.pay.tatra.CardPaySettings;
import sk.seges.corpis.domain.pay.tatra.TatraPayParameter;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class CardPayActivity extends AbstractActivity implements CardPayDisplay.Presenter {

	private ClientFactory clientFactory;
	private CardPayRemoteServiceAsync cardPayRemoteServiceAsync;
	
	public CardPayActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
		this.cardPayRemoteServiceAsync = clientFactory.getCardPayService();
	}

	private static final String RESULT = "OK";
	private static final String APPROVAL_CODE = "TEST";
	
	@Override
	public void start(final AcceptsOneWidget containerWidget, EventBus eventBus) {

		assert Location.getParameter(TatraPayParameter.RURL.getName()) != null;
		assert Location.getParameter(TatraPayParameter.CURR.getName()) != null;
		assert Location.getParameter(TatraPayParameter.NAME.getName()) != null;
		assert Location.getParameter(TatraPayParameter.AMT.getName()) != null;
		assert Location.getParameter(TatraPayParameter.VS.getName()) != null;
		
		final CardPayDisplay cardPayView = clientFactory.getCardPayView();
		
		CardPayRequest cardPayRequest = new CardPayRequest();
		
		CardPaySettings settings = new CardPaySettings();
		settings.setName(Location.getParameter(TatraPayParameter.NAME.getName()));

		cardPayRequest.setSettings(settings);

		Map<String, String> currencies = new HashMap<String, String>();
		currencies.put("978", "€");	//TODO isn't this anywhere defined ?
		currencies.put("703", "SKK");

		cardPayRequest.setCurr(currencies.get(Location.getParameter(TatraPayParameter.CURR.getName())));
		cardPayRequest.setAmt(new BigDecimal(Location.getParameter(TatraPayParameter.AMT.getName())));

		cardPayRequest.setVs(Long.parseLong(Location.getParameter(TatraPayParameter.VS.getName())));
		settings.setRurl(Location.getParameter(TatraPayParameter.RURL.getName()));

		cardPayView.setCardPayRequest(cardPayRequest);
				
		cardPayRemoteServiceAsync.computeSign("" + cardPayRequest.getVs(), RESULT, APPROVAL_CODE, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable cause) {
				GWT.log(cause.getMessage());
			}

			@Override
			public void onSuccess(String sign) {
				cardPayView.setSing(sign, RESULT, APPROVAL_CODE);
				containerWidget.setWidget(cardPayView.asWidget());
			}
			
		});
	}

	@Override
	public void goTo(Place place) {
		clientFactory.getPlaceController().goTo(place);
	}
}