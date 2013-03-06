package sk.seges.corpis.server.domain.payment;

import java.util.Date;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.invoice.Invoice;
import sk.seges.sesam.domain.IDomainObject;

@DomainInterface
@BaseObject
public interface Payment extends IDomainObject<Long> {

	Date paymentDate();

	Invoice invoice();
}
