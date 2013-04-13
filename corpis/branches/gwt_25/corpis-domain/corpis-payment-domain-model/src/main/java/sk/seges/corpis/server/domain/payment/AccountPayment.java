package sk.seges.corpis.server.domain.payment;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.shared.domain.finance.api.ECurrency;

@DomainInterface
@BaseObject
public interface AccountPayment extends Payment {

	Double amount();

	long bankSuffix();

	long cSymbol();

	long sSymbol();

	long vSymbol();

	ECurrency currency();

}