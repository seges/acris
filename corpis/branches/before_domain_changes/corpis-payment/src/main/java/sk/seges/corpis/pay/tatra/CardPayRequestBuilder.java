/**
 * 
 */
package sk.seges.corpis.pay.tatra;

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

import sk.seges.corpis.domain.pay.PaymentRequest;
import sk.seges.corpis.domain.pay.tatra.CardPayRequest;
import sk.seges.corpis.domain.pay.tatra.TatraPayParameter;
import sk.seges.corpis.pay.JSRValidatorAwarePaymentRequestBuilder;
import sk.seges.corpis.pay.signer.PaymentSigner;

/**
 * @author ladislav.gazo
 */
public class CardPayRequestBuilder extends JSRValidatorAwarePaymentRequestBuilder<CardPayRequest> {
	private final PaymentSigner signer;
	
	public CardPayRequestBuilder(Validator validator, PaymentSigner signer) {
		super(validator);
		this.signer = signer;
	}
	
	@Override
	public PaymentRequest generate(CardPayRequest request) {		
		Map<String, String> params = new LinkedHashMap<String, String>();
				
		NumberFormat ff = new DecimalFormat("##0.00", DecimalFormatSymbols.getInstance(new Locale("en")));
		String amt = ff.format(request.getAmt());

		//required parameters
		fill(params, TatraPayParameter.MID.getName(), request.getSettings().getMid());
		fill(params, TatraPayParameter.AMT.getName(), amt);
		fill(params, TatraPayParameter.CURR.getName(), request.getCurr());
		fill(params, TatraPayParameter.VS.getName(), request.getVs());
		fill(params, TatraPayParameter.CS.getName(), request.getCs());
		fill(params, TatraPayParameter.RURL.getName(), request.getSettings().getRurl());
		fill(params, TatraPayParameter.IPC.getName(), request.getIpc());
		fill(params, TatraPayParameter.NAME.getName(), request.getSettings().getName());
		String signature = signer.forgeSignature(request.getSettings(), params.values());
		request.setSign(signature);
		fill(params, TatraPayParameter.SIGN.getName(), signature);
		
		// optional parameters
		fill(params, TatraPayParameter.PT.getName(), request.getSettings().getPt());
		fill(params, TatraPayParameter.DESC.getName(), request.getDesc());
		fill(params, TatraPayParameter.LANG.getName(), request.getSettings().getLang());
		fill(params, TatraPayParameter.REM.getName(), request.getSettings().getRem());
		fill(params, TatraPayParameter.RSMS.getName(), request.getSettings().getRsms());
		fill(params, TatraPayParameter.AREDIR.getName(), request.getSettings().getAredir());

		Set<ConstraintViolation<CardPayRequest>> violations = validate(request);
		if(violations.size() > 0) {
			throw new ValidationException("Request contains validation errors = " + violations);
		}
		
//		StringBuilder builder = new StringBuilder(100);
//		builder.append(request.getSettings().getBaseUrl()); // url
//		builder.append("?");
//
//		add(builder, params);
		
		PaymentRequest paymentRequest = new PaymentRequest(METHOD_POST, request.getSettings().getBaseUrl(), params, "payment-cardPay");
		
		return paymentRequest;
	}
}
