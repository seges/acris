package sk.seges.corpis.server.domain.invoice;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;

@DomainInterface
@BaseObject
public interface OrderItem<O extends Order> extends AccountableItem {

	O order();
}