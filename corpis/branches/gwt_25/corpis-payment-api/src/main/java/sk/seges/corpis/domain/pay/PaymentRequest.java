/**
 * 
 */
package sk.seges.corpis.domain.pay;

import java.util.Map;

/**
 * @author ladislav.gazo
 */
public class PaymentRequest {
	private static final long serialVersionUID = 7283067971179215106L;
	
	private String method;
	private String action;
	private Map<String, String> parameters;
	private String paymentStyleClass;
	
	public PaymentRequest() {
	}
	
	public PaymentRequest(String method, String action, Map<String, String> parameters, String paymentStyleClass) {
		super();
		this.method = method;
		this.action = action;
		this.parameters = parameters;
		this.paymentStyleClass = paymentStyleClass;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	
	public String getPaymentStyleClass() {
		return paymentStyleClass;
	}
	
	public void setPaymentStyleClass(String paymentStyleClass) {
		this.paymentStyleClass = paymentStyleClass;
	}

	@Override
	public String toString() {
		return "PaymentRequest [action=" + action + ", method=" + method + ", parameters=" + parameters
				+ ", paymentStyleClass=" + paymentStyleClass + "]";
	}
}
