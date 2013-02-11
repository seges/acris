package sk.seges.corpis.shared.domain;


/**
 * @author spok
 */
public enum EPaymentType {

	WIRE_TRANSFER("paymentType.wireTransfer", false), 
	PAYPAL("paymentType.paypal", true), 
	VUB_EPLATBA("paymentType.vubEPlatba", true),
	SPOROPAY("paymentType.sporopay", true),
	CARDPAY("paymentType.cardpay", true),
	TATRAPAY("paymentType.tatrapay", true),
	VBPAY("paymentType.vbpay", true),
	UCBPAY("paymentType.ucbpay", true), 
	COD("paymentType.cod", false),
	CASH("paymentType.cash", false);

	private EPaymentType(String i18nKey, boolean electronic) {
		this.i18nKey = i18nKey;
		this.electronic = electronic;
	}

	public String getI18nKey() {
		return i18nKey;
	}
	
	public boolean isElectronic() {
		return electronic;
	}

	private String i18nKey;
	private boolean electronic;
}
