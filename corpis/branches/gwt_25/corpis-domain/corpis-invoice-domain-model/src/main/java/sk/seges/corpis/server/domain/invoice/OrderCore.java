package sk.seges.corpis.server.domain.invoice;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.Address;
import sk.seges.corpis.server.domain.BasicContact;
import sk.seges.corpis.shared.domain.EPaymentType;
import sk.seges.corpis.shared.domain.invoice.ETransports;

@DomainInterface
@BaseObject
public interface OrderCore extends Accountable {

	String orderId();
	String note();
	ETransports deliveredBy();
	String trackingNumber();
	OrderStatus status();

	Boolean sameDeliveryAddress();

	DeliveryPerson deliveryPerson();
	BasicContact deliveryContact();
	Address deliveryAddress();
	Double deliveryPrice();	
	
	String ico();
	String icDph();
	EPaymentType paymentType();
	String accountNumber();
	String projectNumber();
}