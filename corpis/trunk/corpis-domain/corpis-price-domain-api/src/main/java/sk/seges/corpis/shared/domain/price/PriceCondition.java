package sk.seges.corpis.shared.domain.price;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.customer.Customer;
import sk.seges.corpis.server.domain.invoice.Product;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
public interface PriceCondition extends IMutableDomainObject<Long>  {

	public static final String CTX_PAYMENT_DISCOUNT_PRICE_CONDITION = "paymentDiscountPriceConditionType";
	public static final String CTX_ORDER_TYPE_DISCOUNT_PRICE_CONDITION = "orderTypeDiscountPriceCondition";
	
	Double value();
	
	String webId();
	
	Customer customer();
	
	Product product();
	
}
