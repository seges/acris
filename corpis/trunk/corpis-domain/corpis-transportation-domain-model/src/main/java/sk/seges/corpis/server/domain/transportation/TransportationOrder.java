package sk.seges.corpis.server.domain.transportation;

import java.util.Date;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.PersonName;
import sk.seges.corpis.server.domain.invoice.AccountableItem;
import sk.seges.sesam.domain.IDomainObject;

@DomainInterface
@BaseObject
public interface TransportationOrder extends IDomainObject<Long> {

	PersonName person();
	
	AccountableItem accountableItem();
	
	Date orderDate();
	
	Transportation transportation();
}