package sk.seges.corpis.server.domain.stock;

import java.util.Date;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.invoice.AccountableItem;
import sk.seges.sesam.domain.IDomainObject;

@DomainInterface
@BaseObject
public interface StockItemReservation extends IDomainObject<Long> {

	AccountableItem accountableItem();
	
	int count();
	
	Date reservationDate();
	
	StockItem stockItem();
}
