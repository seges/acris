package sk.seges.corpis.server.domain.manufacture;

import java.util.Date;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.PersonName;
import sk.seges.corpis.server.domain.invoice.AccountableItem;
import sk.seges.corpis.server.domain.stock.StockItem;
import sk.seges.sesam.domain.IDomainObject;

@DomainInterface
@BaseObject
public interface ManufactureOrder extends IDomainObject<Long> {

	Date orderDate();
	
	AccountableItem orderItem();
	
	StockItem stockItem();
	
	PersonName responsiblePerson();
}
