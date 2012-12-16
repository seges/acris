package sk.seges.acris.test.client.cardpay.view;

import sk.seges.acris.test.client.cardpay.display.CardPayDisplay;
import sk.seges.corpis.domain.shared.pay.tatra.CardPayRequest;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.FormElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class CardPayView extends Composite implements CardPayDisplay {

	interface CardPayUiBinder extends UiBinder<Widget, CardPayView> {}

	private static CardPayUiBinder uiBinder = GWT.create(CardPayUiBinder.class);

	@UiField
	SpanElement merchant;

	@UiField
	SpanElement transaction;

	@UiField
	SpanElement amount;

	@UiField
	InputElement submit;

	@UiField
	InputElement vs;

	@UiField
	InputElement result;

	@UiField
	InputElement approvalCode;
	
	@UiField
	InputElement sign;

	@UiField
	FormElement confirmationForm;
	
	public CardPayView() {
		initWidget(uiBinder.createAndBindUi(this));
		submit.setDisabled(true);
	}

	@Override
	public void setCardPayRequest(CardPayRequest request) {
		merchant.setInnerHTML(request.getSettings().getName());
		transaction.setInnerHTML("1");
		amount.setInnerHTML(request.getCurr() + " " + request.getAmt());
		vs.setValue("" + request.getVs());
		if (this.approvalCode.getValue() != null && this.approvalCode.getValue().length() > 0) {
			submit.setDisabled(false);
		}
		confirmationForm.setAction(request.getSettings().getRurl());
	}

	@Override
	public void setSing(String sign, String result, String approvalCode) {
		this.result.setValue(result);
		this.approvalCode.setValue(approvalCode);
		this.sign.setValue(sign);
		if (this.amount.getInnerHTML() != null && this.amount.getInnerHTML().length() > 0) {
			submit.setDisabled(false);
		}
}
}