package sk.seges.corpis.server.domain.transportation;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.invoice.AccountableItem;

@DomainInterface
@BaseObject
public interface OrderItemTransportationOrder extends TransportationOrder {

	AccountableItem accountableItem();
}
