package sk.seges.corpis.server.domain.invoice;

import java.util.Date;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.Currency;
import sk.seges.corpis.server.domain.customer.CustomerCore;
import sk.seges.sesam.domain.IDomainObject;

@DomainInterface
@BaseObject
public interface Accountable extends IDomainObject<Long> {

	CustomerCore customer();
	Date creationDate();
	Currency currency();
	String processId();
}