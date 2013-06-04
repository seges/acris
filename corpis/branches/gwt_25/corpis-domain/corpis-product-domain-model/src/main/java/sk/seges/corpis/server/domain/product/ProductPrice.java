package sk.seges.corpis.server.domain.product;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.Price;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
public interface ProductPrice extends IMutableDomainObject<Long> {

	Price price();

	Short priority();

	String externalId();

	ProductPriceCondition priceCondition();
}
