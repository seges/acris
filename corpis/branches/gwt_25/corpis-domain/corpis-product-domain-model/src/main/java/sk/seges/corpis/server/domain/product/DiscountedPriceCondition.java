package sk.seges.corpis.server.domain.product;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.shared.domain.price.api.TerminalPriceCondition;

@DomainInterface
@BaseObject
public interface DiscountedPriceCondition extends ProductPriceCondition, TerminalPriceCondition {}