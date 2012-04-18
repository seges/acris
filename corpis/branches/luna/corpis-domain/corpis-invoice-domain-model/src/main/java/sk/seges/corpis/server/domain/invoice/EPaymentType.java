/**
 * 
 */
package sk.seges.corpis.server.domain.invoice;


/**
 * @author spok
 */
public enum EPaymentType {

	WIRE_TRANSFER("paymentType.wireTransfer"), 
	PAYPAL("paymentType.paypal"), 
	TATRAPAY("paymentType.tatrapay");

	private EPaymentType(String i18nKey) {
		this.i18nKey = i18nKey;
	}

	public String getI18nKey() {
		return i18nKey;
	}

	private String i18nKey;

}
