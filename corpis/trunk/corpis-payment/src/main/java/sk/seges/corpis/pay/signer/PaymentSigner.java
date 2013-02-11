/**
 * 
 */
package sk.seges.corpis.pay.signer;

import java.util.Collection;

import sk.seges.corpis.domain.shared.pay.PaymentMethodSettings;

/**
 * @author ladislav.gazo
 */
public interface PaymentSigner {
	String forgeSignature(PaymentMethodSettings settings, Collection<String> parametersInOrder);
}
