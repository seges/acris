/**
 * 
 */
package sk.seges.corpis.pay.signer;

import java.util.Collection;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import sk.seges.corpis.domain.pay.HasKeyPaymentMethodSettings;
import sk.seges.corpis.domain.pay.PaymentMethodSettings;
import sk.seges.corpis.pay.SignatureHelper;

/**
 * @author ladislav.gazo
 * 
 */
public class HmacSigner implements PaymentSigner {

	private static final String ALGORITHM = "HmacSHA256";

	@Override
	public String forgeSignature(PaymentMethodSettings settings, Collection<String> parametersInOrder) {
		if (!(settings instanceof HasKeyPaymentMethodSettings)) {
			throw new RuntimeException("Unable to forge signature for this type of settings = "
					+ settings.getClass().getName() + ", value = " + settings);
		}

		StringBuilder builder = new StringBuilder();
		for (String param : parametersInOrder) {
			if (param != null) {
				builder.append(param);
			}
		}
		String paramStr = builder.toString();
		HasKeyPaymentMethodSettings keySettings = (HasKeyPaymentMethodSettings) settings;

		SecretKeySpec keySpec = new SecretKeySpec(keySettings.getKey().getBytes(), ALGORITHM);
		try {
			Mac mac = Mac.getInstance(ALGORITHM);
			mac.init(keySpec);
			byte[] message = builder.toString().getBytes();
			byte[] bSIGN = mac.doFinal(message);
			return SignatureHelper.byteArrayToHexString(bSIGN);
		} catch (Exception e) {
			throw new RuntimeException("Unable to create signature from params = " + paramStr, e);
		}
	}

}
