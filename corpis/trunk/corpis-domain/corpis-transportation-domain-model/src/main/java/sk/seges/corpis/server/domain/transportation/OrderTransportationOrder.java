package sk.seges.corpis.server.domain.transportation;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.invoice.Accountable;

@DomainInterface
@BaseObject
public interface OrderTransportationOrder extends TransportationOrder {

	Accountable accountable();
}
