package sk.seges.corpis.server.domain.payment;

import java.util.Date;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
public interface Payment extends IMutableDomainObject<Long> {

	Date paymentDate();

//	Invoice invoice();
}