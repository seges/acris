package sk.seges.corpis.server.domain.payment;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.Country;
import sk.seges.corpis.server.domain.HasWebId;
import sk.seges.corpis.server.domain.Price;
import sk.seges.corpis.server.domain.Vat;
import sk.seges.corpis.server.domain.invoice.Delivery;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
public interface Pad extends IMutableDomainObject<Long>, HasWebId {

	String locale();

	PaymentMethod paymentMethod();

	Delivery delivery();

	Price price();

	Vat vat();

	Country country();
}