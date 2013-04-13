package sk.seges.corpis.server.domain.product.jpa;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import sk.seges.corpis.server.domain.customer.server.model.data.CustomerCoreData;
import sk.seges.corpis.server.domain.product.PriceCondition;
import sk.seges.corpis.server.domain.product.server.model.data.PaymentTypeDiscountPriceConditionData;
import sk.seges.corpis.server.domain.product.server.model.data.ProductData;
import sk.seges.corpis.shared.domain.EPaymentType;
import sk.seges.corpis.shared.domain.price.api.PriceConditionContext;

@Entity
@DiscriminatorValue("2")
@Table
public class JpaPaymentTypeDiscountPriceCondition extends JpaPriceCondition implements PaymentTypeDiscountPriceConditionData {
	private static final long serialVersionUID = 9119082004533756929L;

	private EPaymentType paymentType;
	
	@Column(name = PAYMENT_TYPE)
	@Enumerated(EnumType.STRING)
	public EPaymentType getPaymentType() {
		return paymentType;
	}
	
	public void setPaymentType(EPaymentType paymentType) {
		this.paymentType = paymentType;
	}
	
	@Override
	@Transient
	public String getName() {
		return getPaymentType().name();
	}
	
	@Override
	public boolean applies(PriceConditionContext context, String webId, CustomerCoreData customer, ProductData product) {
		return (super.applies(context, webId, customer, product) && context.get(PriceCondition.CTX_PAYMENT_DISCOUNT_PRICE_CONDITION) != null
				&& context.get(PriceCondition.CTX_PAYMENT_DISCOUNT_PRICE_CONDITION).equals(getPaymentType()));
	}
}