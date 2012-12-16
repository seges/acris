/**
 * 
 */
package sk.seges.corpis.pay.signer;

import java.util.Arrays;
import java.util.Collection;

import sk.seges.corpis.domain.shared.pay.HasKeyPaymentMethodSettings;
import sk.seges.corpis.domain.shared.pay.PaymentMethodSettings;
import sk.seges.corpis.pay.SignatureHelper;

/**
 * @author ladislav.gazo
 */
public class DESSigner implements PaymentSigner {
	@Override
	public String forgeSignature(PaymentMethodSettings settings, Collection<String> parametersInOrder) {
		if(!(settings instanceof HasKeyPaymentMethodSettings)) {
			throw new RuntimeException("Unable to forge signature for this type of settings = " + settings.getClass().getName() + ", value = " + settings);
		}
		
		StringBuilder builder = new StringBuilder();
		for (String param : parametersInOrder) {
			if (param != null) {
				builder.append(param);
			}
		}
		
		HasKeyPaymentMethodSettings keySettings = (HasKeyPaymentMethodSettings) settings;
		
		return SignatureHelper.byteArrayToHexString(Arrays.copyOf(
				SignatureHelper.des(keySettings.getKey(), Arrays.copyOf(SignatureHelper.sha1(builder.toString()), 8)), 8));
	}
}
