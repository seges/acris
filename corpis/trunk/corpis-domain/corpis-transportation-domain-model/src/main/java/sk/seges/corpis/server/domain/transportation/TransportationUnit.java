package sk.seges.corpis.server.domain.transportation;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.Address;
import sk.seges.corpis.server.domain.PersonName;
import sk.seges.corpis.server.domain.stock.StockItem;
import sk.seges.sesam.domain.IDomainObject;

@DomainInterface
@BaseObject
public interface TransportationUnit extends IDomainObject<Long> {

	StockItem stockItem();

	int count();

	Address  deliveryAddress();
	PersonName deliveryPerson();

}