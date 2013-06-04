package sk.seges.corpis.server.domain.product;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;

@DomainInterface
@BaseObject
public interface CountRangeProductPriceCondition extends ProductPriceCondition {

    Integer lowerBoundary();

    Integer upperBoundary();

}