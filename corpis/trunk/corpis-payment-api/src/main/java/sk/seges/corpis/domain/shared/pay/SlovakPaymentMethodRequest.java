/**
 * 
 */
package sk.seges.corpis.domain.shared.pay;

import java.math.BigDecimal;

/**
 * @author ladislav.gazo
 */
public interface SlovakPaymentMethodRequest extends PaymentMethodRequest {
	void setVs(Long vs);
	void setAmt(BigDecimal amt);
}
