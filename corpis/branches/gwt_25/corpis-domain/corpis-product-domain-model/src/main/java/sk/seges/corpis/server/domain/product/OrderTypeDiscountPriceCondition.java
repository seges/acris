package sk.seges.corpis.server.domain.product;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.shared.domain.customer.ECustomerDiscountType;

@DomainInterface
@BaseObject
public interface OrderTypeDiscountPriceCondition extends PriceCondition {

	ECustomerDiscountType orderType();

}