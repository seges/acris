package sk.seges.corpis.server.domain.invoice;

import java.util.Date;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.customer.Customer;
import sk.seges.sesam.domain.IDomainObject;

@DomainInterface
@BaseObject
public interface Accountable extends IDomainObject<Long> {

	Customer customer();
	Date creationDate();
	Currency currency();
	String processId();
}