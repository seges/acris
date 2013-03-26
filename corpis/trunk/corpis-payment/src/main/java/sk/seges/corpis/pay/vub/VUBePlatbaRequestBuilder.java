/**
 * 
 */
package sk.seges.corpis.pay.vub;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;

import sk.seges.corpis.domain.shared.pay.PaymentRequest;
import sk.seges.corpis.domain.shared.pay.vub.VUBePlatbaParameter;
import sk.seges.corpis.domain.shared.pay.vub.VUBePlatbaRequest;
import sk.seges.corpis.pay.JSRValidatorAwarePaymentRequestBuilder;
import sk.seges.corpis.pay.PaymentConstants;
import sk.seges.corpis.pay.signer.PaymentSigner;

/**
 * @author ladislav.gazo
 */
public class VUBePlatbaRequestBuilder extends JSRValidatorAwarePaymentRequestBuilder<VUBePlatbaRequest> {
	private final PaymentSigner signer;
	
	public VUBePlatbaRequestBuilder(Validator validator, PaymentSigner signer) {
		super(validator);
		this.signer = signer;
	}

	@Override
	public PaymentRequest generate(VUBePlatbaRequest request) {
		Map<String, String> params = new LinkedHashMap<String, String>();

		NumberFormat ff = new DecimalFormat("##0.00", DecimalFormatSymbols.getInstance(new Locale("en")));
		String amt = ff.format(request.getAmt());

		// required parameters
		fill(params, VUBePlatbaParameter.MID.getName(), request.getSettings().getMid());
		fill(params, VUBePlatbaParameter.AMT.getName(), amt);
		fill(params, VUBePlatbaParameter.VS.getName(), request.getVs());
		fill(params, VUBePlatbaParameter.CS.getName(), PaymentConstants.CONSTANT_SYMBOL_DEFAULT);
		fill(params, VUBePlatbaParameter.RURL.getName(), request.getSettings().getRurl());
		String signature = signer.forgeSignature(request.getSettings(), params.values());
		request.setSign(signature);
		fill(params, VUBePlatbaParameter.SIGN.getName(), signature);

		// optional parameters
		fill(params, VUBePlatbaParameter.SS.getName(), request.getSs());
		fill(params, VUBePlatbaParameter.DESC.getName(), request.getDesc());
		fill(params, VUBePlatbaParameter.REM.getName(), request.getSettings().getRem());
		fill(params, VUBePlatbaParameter.RSMS.getName(), request.getSettings().getRsms());

		Set<ConstraintViolation<VUBePlatbaRequest>> violations = validate(request);
		if (violations.size() > 0) {
			throw new ValidationException("Request contains validation errors = " + violations);
		}

		// StringBuilder builder = new StringBuilder(100);
		// builder.append(request.getSettings().getBaseUrl()); // url
		// builder.append("?");
		//
		// add(builder, params);

		PaymentRequest paymentRequest = new PaymentRequest(METHOD_POST, request.getSettings().getBaseUrl(), params,
				"payment-VUBePlatba");

		return paymentRequest;
	}
}
