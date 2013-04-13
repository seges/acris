package sk.seges.corpis.server.domain.invoice;

import java.util.List;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
public interface OrderItem extends AccountableItem, IMutableDomainObject<Long> {

	Order order();
	
	List<InvoiceItem> invoiceItems();
}