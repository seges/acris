package sk.seges.corpis.domain.price.jpa;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.customer.Customer;
import sk.seges.corpis.server.domain.invoice.Product;
import sk.seges.corpis.shared.domain.EPaymentType;
import sk.seges.corpis.shared.domain.price.PriceCondition;
import sk.seges.corpis.shared.domain.price.api.PriceConditionContext;

@Entity
@DiscriminatorValue("2")
@Table
public class JpaPaymentTypeDiscountPriceCondition extends JpaPriceCondition {
	private static final long serialVersionUID = 9119082004533756929L;

	private EPaymentType paymentType;
	
	public JpaPaymentTypeDiscountPriceCondition(EPaymentType paymentType) {
		this.paymentType = paymentType;
	}
	
	@Column
	@Enumerated(EnumType.STRING)
	public EPaymentType getPaymentType() {
		return paymentType;
	}
	
	@Override
	public boolean applies(PriceConditionContext context, String webId, Customer customer, Product product) {
		return (super.applies(context, webId, customer, product) && context.get(PriceCondition.CTX_PAYMENT_DISCOUNT_PRICE_CONDITION).equals(getPaymentType()));
	}
}
