/**
 * 
 */
package sk.seges.corpis.domain.pay;

import java.io.Serializable;

/**
 * Marker interface for all requests of electronic payment methods.
 * 
 * @author ladislav.gazo
 * 
 */
public interface PaymentMethodRequest extends Serializable {
	void setSettings(PaymentMethodSettings settings);
}
