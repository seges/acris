/**
 * 
 */
package sk.seges.sesam.pap.customer;

import sk.seges.sesam.domain.IMutableDomainObject;

/**
 * @author ladislav.gazo
 */
public interface TestCustomerBaseData<K> extends IMutableDomainObject<K> {
	public static final String ID = "id"; //$NON-NLS-1$

	public static final String SHORTCUT = "shortcut"; //$NON-NLS-1$
	public static final String CONTACTPERSON = "contactPerson"; //$NON-NLS-1$

	public static final String ADDRESS = "address";
	public static final String COMPANY = "company";
	public static final String CONTACT = "contact";
	public static final String PERSON = "person";

	public static final String CORRESP_ADDRESS = "correspAddress";
	public static final String CORRESP_COMPANY = "correspCompany";
	public static final String CORRESP_CONTACT = "correspContact";
	public static final String CORRESP_PERSON = "correspPerson";

	public static final String ICO = "ico"; //$NON-NLS-1$
	public static final String DIC = "dic"; //$NON-NLS-1$
	public static final String ICDPH = "icDph"; //$NON-NLS-1$
	public static final String TAXPAYMENT = "taxPayment"; //$NON-NLS-1$
	public static final String INVOICEPAYMENTINTERVAL = "invoicePaymentInterval"; //$NON-NLS-1$
	public static final String ACCOUNT_NUMBER = "accountNumber";
	
	TestPersonNameData getPerson();
	void setPerson(TestPersonNameData person);
	
	TestCompanyNameData getCompany();
	void setCompany(TestCompanyNameData company);
	
	Boolean getCompanyType();
	void setCompanyType(Boolean isCompany);
	
	TestContactData getContact();
	void setContact(TestContactData contact);
	
	TestAddressData getAddress();
	void setAddress(TestAddressData address);
	
	String getShortcut();
	void setShortcut(String shortcut);

	String getContactPerson();
	void setContactPerson(String contactPerson);

	TestCompanyNameData getCorrespCompany();
	void setCorrespCompany(TestCompanyNameData correspCompany);

	TestPersonNameData getCorrespPerson();
	void setCorrespPerson(TestPersonNameData correspPerson);

	TestAddressData getCorrespAddress();
	void setCorrespAddress(TestAddressData correspAddress);

	TestContactData getCorrespContact();
	void setCorrespContact(TestContactData correspContact);

	String getIco();
	void setIco(String ico);

	String getDic();
	void setDic(String dic);

	String getIcDph();
	void setIcDph(String icDph);

	Boolean getTaxPayment();
	void setTaxPayment(Boolean taxPayment);

	Short getInvoicePaymentInterval();
	void setInvoicePaymentInterval(Short invoicePaymentInterval);

	String getAccountNumber();
	void setAccountNumber(String accountNumber);
}
