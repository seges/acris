package sk.seges.corpis.server.domain.product;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.shared.domain.EPaymentType;

@DomainInterface
@BaseObject
public interface PaymentTypeDiscountPriceCondition extends PriceCondition {

	EPaymentType paymentType();

}