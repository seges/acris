package sk.seges.corpis.shared.domain.invoice.api;

import sk.seges.corpis.shared.domain.customer.api.AddressData;
import sk.seges.corpis.shared.domain.customer.api.BasicContactData;
import sk.seges.corpis.shared.domain.customer.api.CompanyNameData;
import sk.seges.corpis.shared.domain.customer.api.PersonNameData;

public interface OrderData<T> extends AccountableData<T> {

	String getOrderId();

	void setOrderId(String orderId);

	String getNote();

	void setNote(String note);

	ETransports getDeliveredBy();

	void setDeliveredBy(ETransports deliveredBy);

	Double getDeliveryPrice();

	void setDeliveryPrice(Double deliveryPrice);

	String getTrackingNumber();

	void setTrackingNumber(String trackingNumber);

	EOrderStatus getStatus();

	void setStatus(EOrderStatus status);

	CompanyNameData getCompany();

	void setCompany(CompanyNameData company);

	PersonNameData getPerson();

	void setPerson(PersonNameData person);

	AddressData getAddress();

	void setAddress(AddressData address);

	AddressData getDeliveryAddress();

	void setDeliveryAddress(AddressData deliveryAddress);

	BasicContactData getContact();

	void setContact(BasicContactData contact);

	BasicContactData getDeliveryContact();

	void setDeliveryContact(BasicContactData deliveryContact);

	String getIco();

	void setIco(String ico);

	String getIcDph();

	void setIcDph(String icDph);

	EPaymentType getPaymentType();

	void setPaymentType(EPaymentType paymentType);

	String getAccountNumber();

	void setAccountNumber(String accountNumber);

	String getProjectNumber();

	void setProjectNumber(String projectNumber);
}