/**
 * 
 */
package sk.seges.corpis.pay;

import java.util.Set;

import javax.validation.ConstraintViolation;

import sk.seges.corpis.domain.shared.pay.PaymentMethodRequest;
import sk.seges.corpis.domain.shared.pay.PaymentRequest;

/**
 * @author ladislav.gazo
 */
public interface ElectronicPaymentRequestBuilder<T extends PaymentMethodRequest> {
	static final String METHOD_GET = "get";
	static final String METHOD_POST = "post";
	
	PaymentRequest generate(T request);
	Set<ConstraintViolation<T>> validate(T request);
}
