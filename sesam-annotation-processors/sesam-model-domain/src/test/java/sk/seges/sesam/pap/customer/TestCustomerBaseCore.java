/**
 * 
 */
package sk.seges.sesam.pap.customer;


/**
 * @author ladislav.gazo
 */
public class TestCustomerBaseCore<K> implements TestCustomerBaseData<K> {
	protected static final long serialVersionUID = 9135181519367038631L;

	protected K id;
	protected String shortcut;
	protected String contactPerson;

	protected Boolean companyType;
	protected TestCompanyNameData company;
	protected TestPersonNameData person;
	protected TestAddressData address;

	protected TestContactData contact;

	protected TestCompanyNameData correspCompany;
	protected TestPersonNameData correspPerson;
	protected TestAddressData correspAddress;
	protected TestContactData correspContact;

	protected String ico;
	protected String dic;
	protected String icDph;
	protected Boolean taxPayment;
	protected Short invoicePaymentInterval;
	protected String accountNumber;

	@Override
	public TestAddressData getAddress() {
		return address;
	}

	@Override
	public TestCompanyNameData getCompany() {
		return company;
	}

	@Override
	public Boolean getCompanyType() {
		return companyType;
	}

	@Override
	public TestContactData getContact() {
		return contact;
	}

	@Override
	public TestPersonNameData getPerson() {
		return person;
	}

	@Override
	public void setAddress(TestAddressData address) {
		this.address = address;
	}

	@Override
	public void setCompany(TestCompanyNameData company) {
		this.company = company;
	}

	@Override
	public void setCompanyType(Boolean isCompany) {
		this.companyType = isCompany;
	}

	@Override
	public void setContact(TestContactData contact) {
		this.contact = contact;
	}

	@Override
	public void setPerson(TestPersonNameData person) {
		this.person = person;
	}

	@Override
	public void setId(K id) {
		this.id = id;
	}

	@Override
	public K getId() {
		return id;
	}

	@Override
	public String getShortcut() {
		return shortcut;
	}

	@Override
	public void setShortcut(String shortcut) {
		this.shortcut = shortcut;
	}

	@Override
	public String getContactPerson() {
		return contactPerson;
	}

	@Override
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	@Override
	public TestCompanyNameData getCorrespCompany() {
		return correspCompany;
	}

	@Override
	public void setCorrespCompany(TestCompanyNameData correspCompany) {
		this.correspCompany = correspCompany;
	}

	@Override
	public TestPersonNameData getCorrespPerson() {
		return correspPerson;
	}

	@Override
	public void setCorrespPerson(TestPersonNameData correspPerson) {
		this.correspPerson = correspPerson;
	}

	@Override
	public TestAddressData getCorrespAddress() {
		return correspAddress;
	}

	@Override
	public void setCorrespAddress(TestAddressData correspAddress) {
		this.correspAddress = correspAddress;
	}

	@Override
	public TestContactData getCorrespContact() {
		return correspContact;
	}

	@Override
	public void setCorrespContact(TestContactData correspContact) {
		this.correspContact = correspContact;
	}

	@Override
	public String getIco() {
		return ico;
	}

	@Override
	public void setIco(String ico) {
		this.ico = ico;
	}

	@Override
	public String getDic() {
		return dic;
	}

	@Override
	public void setDic(String dic) {
		this.dic = dic;
	}

	@Override
	public String getIcDph() {
		return icDph;
	}

	@Override
	public void setIcDph(String icDph) {
		this.icDph = icDph;
	}

	@Override
	public Boolean getTaxPayment() {
		return taxPayment;
	}

	@Override
	public void setTaxPayment(Boolean taxPayment) {
		this.taxPayment = taxPayment;
	}

	@Override
	public Short getInvoicePaymentInterval() {
		return invoicePaymentInterval;
	}

	@Override
	public void setInvoicePaymentInterval(Short invoicePaymentInterval) {
		this.invoicePaymentInterval = invoicePaymentInterval;
	}

	@Override
	public String getAccountNumber() {
		return accountNumber;
	}

	@Override
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 73;
		int result = 1;
		result = prime * result + ((person == null) ? 0 : person.hashCode())
				+ ((company == null) ? 0 : company.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestCustomerBaseCore<?> other = (TestCustomerBaseCore<?>) obj;
		if (person == null) {
			if (other.person != null)
				return false;
		} else if (!person.equals(other.person))
			return false;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "CustomerBaseDto [accountNumber=" + accountNumber + ", address=" + address + ", company="
				+ company + ", companyType=" + companyType + ", contact=" + contact + ", contactPerson="
				+ contactPerson + ", correspAddress=" + correspAddress + ", correspCompany=" + correspCompany
				+ ", correspContact=" + correspContact + ", correspPerson=" + correspPerson + ", dic=" + dic
				+ ", icDph=" + icDph + ", ico=" + ico + ", id=" + id + ", invoicePaymentInterval="
				+ invoicePaymentInterval + ", person=" + person + ", shortcut=" + shortcut + ", taxPayment="
				+ taxPayment + "]";
	}

}
