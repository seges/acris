package sk.seges.corpis.server.domain.payment.jpa;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.invoice.jpa.JpaWebIDAwareOrderItem;
import sk.seges.corpis.server.domain.payment.server.model.data.PaymentMethodData;

@Entity
@Table(name = "payment_method_order_item")
public class JpaPaymentMethodOrderItem extends JpaWebIDAwareOrderItem {

	private static final long serialVersionUID = -1557928799994501781L;

	private PaymentMethodData paymentMethod;
	
	@ManyToOne(targetEntity = JpaPaymentMethod.class)
	public PaymentMethodData getPaymentMethod() {
		return paymentMethod;
	}
	
	public void setPaymentMethod(PaymentMethodData paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

}
