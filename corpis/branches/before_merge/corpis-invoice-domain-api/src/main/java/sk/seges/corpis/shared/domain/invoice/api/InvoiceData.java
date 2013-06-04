package sk.seges.corpis.shared.domain.invoice.api;

import java.util.Date;
import java.util.Set;

import sk.seges.corpis.shared.domain.api.HasVersion;
import sk.seges.corpis.shared.domain.customer.api.CustomerBaseData;
import sk.seges.corpis.shared.domain.customer.api.PersonNameData;
import sk.seges.sesam.domain.IMutableDomainObject;

public interface InvoiceData<T> extends IMutableDomainObject<T>, HasVersion {

	public static final String PROPERTYNAME_INVOICEID = "invoiceId"; //$NON-NLS-1$
	public static final String PROPERTYNAME_CSYMBOL = "csymbol"; //$NON-NLS-1$
	public static final String PROPERTYNAME_SSYMBOL = "ssymbol"; //$NON-NLS-1$
	public static final String PROPERTYNAME_VSYMBOL = "vsymbol"; //$NON-NLS-1$
	public static final String PROPERTYNAME_PAID = "paid"; //$NON-NLS-1$
	public static final String PROPERTYNAME_PREPAID = "prepaid"; //$NON-NLS-1$
	public static final String PROPERTYNAME_INCOMINGINVOICETYPE = "incomingInvoiceType"; //$NON-NLS-1$
	public static final String PROPERTYNAME_CUSTOMER = "customer"; //$NON-NLS-1$
	public static final String PROPERTYNAME_CREATIONDATE = "creationDate"; //$NON-NLS-1$
	public static final String PROPERTYNAME_PAYBACKDATE = "paybackDate"; //$NON-NLS-1$
	public static final String PROPERTYNAME_TAXDATE = "taxDate"; //$NON-NLS-1$
	public static final String PROPERTYNAME_REMITTANCETYPE = "remittanceType"; //$NON-NLS-1$
	public static final String PROPERTYNAME_TRANSPORTTYPE = "transportType"; //$NON-NLS-1$
	public static final String PROPERTYNAME_PENNYBALANCE = "pennyBalance"; //$NON-NLS-1$
	public static final String PROPERTYNAME_CREATOR = "creator"; //$NON-NLS-1$
	public static final String PROPERTYNAME_INVOICETEXT = "invoiceText"; //$NON-NLS-1$
	public static final String PROPERTYNAME_FINALPRICE = "finalPrice"; //$NON-NLS-1$

	Date getTaxDate();
	void setTaxDate(Date taxDate);

	Date getPaybackDate();
	void setPaybackDate(Date paybackDate);

	Date getCreationDate();
	void setCreationDate(Date creationDate);

	Integer getInvoiceId();
	void setInvoiceId(Integer invoiceId);

	CustomerBaseData<?> getCustomer();
	void setCustomer(CustomerBaseData<?> customer);

	String getCsymbol();
	void setCsymbol(String csymbol);

	String getSsymbol();
	void setSsymbol(String ssymbol);

	String getVsymbol();
	void setVsymbol(String vsymbol);

	Boolean getPaid();
	void setPaid(Boolean paid);

	Boolean getPrepaid();
	void setPrepaid(Boolean prepaid);

	Boolean getIncomingInvoiceType();
	void setIncomingInvoiceType(Boolean incomingInvoiceType);

	Set<InvoiceItemData<?>> getInvoiceItems();
	void setInvoiceItems(Set<InvoiceItemData<?>> invoiceItems);

	Double getPennyBalance();
	void setPennyBalance(Double pennyBalance);

	Set<RemittanceData<?>> getRemittances();
	void setRemittances(Set<RemittanceData<?>> remittances);

	PersonNameData getCreator();
	void setCreator(PersonNameData creator);

	String getInvoiceText();
	void setInvoiceText(String invoiceText);

	Double getFinalPrice();
	void setFinalPrice(Double finalPrice);

	RemittanceType getRemittanceType();
	void setRemittanceType(RemittanceType remittanceType);

	TransportType getTransportType();
	void setTransportType(TransportType transportType);

	Boolean getAddVAT();
	void setAddVAT(Boolean addVAT);
}