package sk.seges.corpis.server.domain.invoice;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.shared.domain.invoice.IProductPriceCondition;
import sk.seges.sesam.domain.IDomainObject;

@DomainInterface
@BaseObject
public interface ProductPriceCondition extends IDomainObject<Long>, IProductPriceCondition {

	String conditionDescription();

}