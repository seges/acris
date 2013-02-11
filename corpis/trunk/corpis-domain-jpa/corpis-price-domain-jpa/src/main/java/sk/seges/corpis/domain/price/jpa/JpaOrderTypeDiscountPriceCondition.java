package sk.seges.corpis.domain.price.jpa;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.customer.Customer;
import sk.seges.corpis.server.domain.invoice.Product;
import sk.seges.corpis.shared.domain.customer.ECustomerDiscountType;
import sk.seges.corpis.shared.domain.price.PriceCondition;
import sk.seges.corpis.shared.domain.price.api.PriceConditionContext;

@Entity
@DiscriminatorValue("3")
@Table
public class JpaOrderTypeDiscountPriceCondition extends JpaPriceCondition {
	private static final long serialVersionUID = -8661927126006818012L;
	
	private ECustomerDiscountType orderType;
	
	public JpaOrderTypeDiscountPriceCondition(ECustomerDiscountType orderType) {
		this.orderType = orderType;
	}

	@Column
	@Enumerated(EnumType.STRING)
	public ECustomerDiscountType getOrderType() {
		return orderType;
	}
	
	@Override
	public boolean applies(PriceConditionContext context, String webId, Customer customer, Product product) {
		return (super.applies(context, webId, customer, product) && context.get(PriceCondition.CTX_ORDER_TYPE_DISCOUNT_PRICE_CONDITION).equals(getOrderType())); 
	}
}
