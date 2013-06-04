package sk.seges.corpis.server.domain.payment;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.customer.CustomerCore;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
public interface BankAccount extends IMutableDomainObject<Long> {

	Bank bank();

	String bankAccount();

	CustomerCore customer();

	String iban();
}