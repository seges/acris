/**
 * 
 */
package sk.seges.corpis.domain.shared.pay;

import sk.seges.sesam.domain.IDomainObject;

/**
 * @author ladislav.gazo
 */
public interface WebIDAwarePaymentMethodSettings extends IDomainObject<Long> {
	static final String SEQ_DB_NAME_PAYMENT_METHOD_SETTINGS = "seq_payment_method_settings";
	static final String SEQ_PAYMENT_METHOD_SETTINGS = "seqPaymentMethodSettings";
	
	static final String ID = "id";
	static final String WEB_ID = "webId";
	static final String SETTINGS = "settings";

	String getWebId();
	void setWebId(String webId);
	PaymentMethodSettings getSettings();
}
