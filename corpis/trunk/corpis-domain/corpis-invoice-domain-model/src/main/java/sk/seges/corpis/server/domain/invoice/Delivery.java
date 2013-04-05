package sk.seges.corpis.server.domain.invoice;

import java.math.BigDecimal;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.Country;
import sk.seges.corpis.shared.domain.invoice.ETransports;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
public interface Delivery extends IMutableDomainObject<Long> {

	String webId();

	ETransports transportType();

	Country country();

	Price price();

	Vat vat();

	BigDecimal priceCondition();

	Boolean priceConditionWithVAT();

	Float amountCondition();

	Float weightCondition();
}