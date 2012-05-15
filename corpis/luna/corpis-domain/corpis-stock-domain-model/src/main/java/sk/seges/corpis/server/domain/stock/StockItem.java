package sk.seges.corpis.server.domain.stock;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.invoice.Product;
import sk.seges.sesam.domain.IDomainObject;

@DomainInterface
@BaseObject
public interface StockItem extends IDomainObject<Long> {

	Product product();
	
	int count();

	boolean inactive();
	
	Warehouse warehouse();

}