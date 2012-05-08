package sk.seges.corpis.server.domain.invoice;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.Address;
import sk.seges.corpis.server.domain.BasicContact;
import sk.seges.corpis.server.domain.CompanyName;
import sk.seges.corpis.server.domain.PersonName;
import sk.seges.corpis.shared.domain.invoice.EOrderStatus;
import sk.seges.corpis.shared.domain.invoice.EPaymentType;
import sk.seges.corpis.shared.domain.invoice.ETransports;
import sk.seges.sesam.domain.IDomainObject;

@DomainInterface
@BaseObject
public interface Order extends IDomainObject<Long> {

	String orderId();
	String note();
	ETransports deliveredBy();
	String trackingNumber();
	EOrderStatus status();
	CompanyName company();

	PersonName person();
	Address address();
	BasicContact contact();

	Boolean sameDeliveryAddress();

	Address deliveryAddress();
	DeliveryPerson deliveryPerson();
	BasicContact deliveryContact();
	
	String ico();
	String icDph();
	EPaymentType paymentType();
	String accountNumber();
	String projectNumber();
}