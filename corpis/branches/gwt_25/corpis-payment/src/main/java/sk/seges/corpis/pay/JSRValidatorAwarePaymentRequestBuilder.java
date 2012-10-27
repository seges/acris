/**
 * 
 */
package sk.seges.corpis.pay;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import sk.seges.corpis.domain.pay.PaymentMethodRequest;

/**
 * @author ladislav.gazo
 */
public abstract class JSRValidatorAwarePaymentRequestBuilder<T extends PaymentMethodRequest> extends
		AbstractPaymentRequestBuilder<T> {

	private final Validator validator;
	private Class<?>[] groups;
	
	public JSRValidatorAwarePaymentRequestBuilder(Validator validator) {
		super();
		this.validator = validator;
	}
	
	public void setGroups(Class<?>[] groups) {
		this.groups = groups;
	}

	/* (non-Javadoc)
	 * @see sk.seges.corpis.pay.ElectronicPaymentRequestBuilder#validate(sk.seges.corpis.domain.PaymentMethodRequest)
	 */
	@Override
	public Set<ConstraintViolation<T>> validate(T request) {
		if(groups != null) {
			return validator.validate(request, groups);
		}
		return validator.validate(request);
	}
}
