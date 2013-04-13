package sk.seges.corpis.server.domain.invoice;

import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;

@DomainInterface
public interface DeliveryOrderItem extends OrderItem {

	Delivery delivery();

}
