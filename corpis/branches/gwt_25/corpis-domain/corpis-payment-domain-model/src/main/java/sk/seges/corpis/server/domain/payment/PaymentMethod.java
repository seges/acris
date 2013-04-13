package sk.seges.corpis.server.domain.payment;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.Country;
import sk.seges.corpis.server.domain.Price;
import sk.seges.corpis.server.domain.Vat;
import sk.seges.corpis.shared.domain.EPaymentType;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
public interface PaymentMethod extends IMutableDomainObject<Long> {

	String webId();

	EPaymentType paymentType();

	Country country();

	Price price();
    
	Vat vat();
}