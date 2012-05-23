package sk.seges.corpis.server.domain.customer;

import java.util.Date;
import java.util.Map;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.Address;
import sk.seges.corpis.server.domain.BasicContact;
import sk.seges.corpis.server.domain.CompanyName;
import sk.seges.corpis.server.domain.PersonName;
import sk.seges.corpis.shared.domain.customer.ECustomerDiscountType;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
public interface Customer extends IMutableDomainObject<Long> {

	String shortcut();
	String contactPerson();

	Boolean companyType();
	CompanyName company();
	PersonName person();
	Address address();

	BasicContact contact();

	CompanyName correspCompany();
	PersonName correspPerson();
	Address correspAddress();
	BasicContact correspContact();

	String ico();
	String dic();
	String icDph();
	Boolean taxPayment();
	Short invoicePaymentInterval();
	String accountNumber();
	Date registrationDate();
	
	Map<ECustomerDiscountType, Double> discounts();

	Boolean commision();
	
}