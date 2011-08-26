package sk.seges.acris.test.client.cardpay.display;

import sk.seges.corpis.domain.pay.tatra.CardPayRequest;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface CardPayDisplay extends IsWidget {

	public void setCardPayRequest(CardPayRequest request);
	public void setSing(String sign, String result, String approvalCode);
	
	public interface Presenter {
		void goTo(Place place);
	}

}