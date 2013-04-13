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
import sk.seges.corpis.server.domain.product.server.model.data.OrderTypeDiscountPriceConditionData;
import sk.seges.corpis.server.domain.product.server.model.data.ProductData;
import sk.seges.corpis.shared.domain.customer.ECustomerDiscountType;
import sk.seges.corpis.shared.domain.price.api.PriceConditionContext;

@Entity
@DiscriminatorValue("3")
@Table
public class JpaOrderTypeDiscountPriceCondition extends JpaPriceCondition implements OrderTypeDiscountPriceConditionData {
	private static final long serialVersionUID = -8661927126006818012L;
	
	private ECustomerDiscountType orderType;
	
	@Column(name = OrderTypeDiscountPriceConditionData.ORDER_TYPE)
	@Enumerated(EnumType.STRING)
	public ECustomerDiscountType getOrderType() {
		return orderType;
	}
	
	@Override
	@Transient
	public String getName() {
		return getOrderType().name();
	}
	
	@Override
	public boolean applies(PriceConditionContext context, String webId, CustomerCoreData customer, ProductData product) {
		return (super.applies(context, webId, customer, product) && context.get(PriceCondition.CTX_ORDER_TYPE_DISCOUNT_PRICE_CONDITION) != null &&
				context.get(PriceCondition.CTX_ORDER_TYPE_DISCOUNT_PRICE_CONDITION).equals(getOrderType()));
	}

	@Override
	public void setOrderType(ECustomerDiscountType orderType) {
		this.orderType = orderType;
	}
}