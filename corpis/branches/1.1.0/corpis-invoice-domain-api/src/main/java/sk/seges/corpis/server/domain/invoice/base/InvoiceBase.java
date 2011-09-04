package sk.seges.corpis.server.domain.invoice.base;

import java.beans.PropertyChangeSupport;
import java.util.Date;
import java.util.Set;

import sk.seges.corpis.shared.domain.customer.api.CustomerBaseData;
import sk.seges.corpis.shared.domain.customer.api.PersonNameData;
import sk.seges.corpis.shared.domain.invoice.api.InvoiceData;
import sk.seges.corpis.shared.domain.invoice.api.InvoiceItemData;
import sk.seges.corpis.shared.domain.invoice.api.RemittanceData;
import sk.seges.corpis.shared.domain.invoice.api.RemittanceType;
import sk.seges.corpis.shared.domain.invoice.api.TransportType;

@SuppressWarnings("serial")
public class InvoiceBase<T> implements InvoiceData<T> {

	private T id;

	private Integer invoiceId;

	private String csymbol;

	private String ssymbol;

	private String vsymbol;

	private Boolean paid = Boolean.FALSE;

	private Boolean prepaid;

	private Boolean incomingInvoiceType;

	private Set<InvoiceItemData<?>> invoiceItems;

	private Integer version;

	private CustomerBaseData<?> customer;

	private Date creationDate;

	private Date paybackDate;

	private Date taxDate;

	private RemittanceType remittanceType;

	private TransportType transportType;

	private Set<RemittanceData<?>> remittances;

	private Double pennyBalance;

	private PersonNameData creator;

	private String invoiceText;

	private Boolean addVAT;
	
	transient protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	private Double finalPrice;
	
	public InvoiceBase() {
	}

	@Override
	public Double getFinalPrice() {
		return finalPrice;
	}
	
	@Override
	public void setFinalPrice(Double finalPrice) {
		this.finalPrice = finalPrice;
	}
	
	public Date getTaxDate() {
		return taxDate;
	}

	public void setTaxDate(Date taxDate) {
		Date oldValue = this.taxDate;
		this.taxDate = taxDate;
		pcs.firePropertyChange(PROPERTYNAME_TAXDATE, oldValue, taxDate);
	}

	public Date getPaybackDate() {
		return paybackDate;
	}

	public void setPaybackDate(Date paybackDate) {
		Date oldValue = this.paybackDate;
		this.paybackDate = paybackDate;
		pcs.firePropertyChange(PROPERTYNAME_PAYBACKDATE, oldValue, paybackDate);
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		Date oldValue = this.creationDate;
		this.creationDate = creationDate;
		pcs.firePropertyChange(PROPERTYNAME_CREATIONDATE, oldValue, creationDate);
	}

	public Integer getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Integer invoiceId) {
		Integer oldValue = this.invoiceId;
		this.invoiceId = invoiceId;
		pcs.firePropertyChange(PROPERTYNAME_INVOICEID, oldValue, this.invoiceId);
	}

	public CustomerBaseData<?> getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerBaseData<?> customer) {
		CustomerBaseData<?> oldValue = this.customer;
		this.customer = customer;
		pcs.firePropertyChange(PROPERTYNAME_CUSTOMER, oldValue, customer);
	}

	public T getId() {
		return id;
	}

	public void setId(T id) {
		this.id = id;
	}

	public String getCsymbol() {
		return csymbol;
	}

	public void setCsymbol(String csymbol) {
		String oldValue = this.csymbol;
		this.csymbol = csymbol;
		pcs.firePropertyChange(PROPERTYNAME_CSYMBOL, oldValue, csymbol);
	}

	public String getSsymbol() {
		return ssymbol;
	}

	public void setSsymbol(String ssymbol) {
		String oldValue = this.ssymbol;
		this.ssymbol = ssymbol;
		pcs.firePropertyChange(PROPERTYNAME_SSYMBOL, oldValue, ssymbol);
	}

	public String getVsymbol() {
		return vsymbol;
	}

	public void setVsymbol(String vsymbol) {
		String oldValue = this.vsymbol;
		this.vsymbol = vsymbol;
		pcs.firePropertyChange(PROPERTYNAME_VSYMBOL, oldValue, vsymbol);
	}

	public Boolean getPaid() {
		return paid;
	}

	public void setPaid(Boolean paid) {
		Boolean oldValue = this.paid;
		this.paid = paid;
		pcs.firePropertyChange(PROPERTYNAME_PAID, oldValue, paid);
	}

	public Boolean getPrepaid() {
		return prepaid;
	}

	public void setPrepaid(Boolean prepaid) {
		Boolean oldValue = this.prepaid;
		this.prepaid = prepaid;
		pcs.firePropertyChange(PROPERTYNAME_PREPAID, oldValue, prepaid);
	}

	public Boolean getIncomingInvoiceType() {
		return incomingInvoiceType;
	}

	public void setIncomingInvoiceType(Boolean incomingInvoiceType) {
		Boolean oldValue = this.incomingInvoiceType;
		this.incomingInvoiceType = incomingInvoiceType;
		pcs.firePropertyChange(PROPERTYNAME_INCOMINGINVOICETYPE, oldValue,
				incomingInvoiceType);
	}

	public Set<InvoiceItemData<?>> getInvoiceItems() {
		return invoiceItems;
	}

	public void setInvoiceItems(Set<InvoiceItemData<?>> invoiceItems) {
		this.invoiceItems = invoiceItems;
	}

	public Double getPennyBalance() {
		return pennyBalance;
	}

	public void setPennyBalance(Double pennyBalance) {
	Double oldValue = this.pennyBalance;
		this.pennyBalance = pennyBalance;
		pcs.firePropertyChange(PROPERTYNAME_PENNYBALANCE, oldValue, pennyBalance);
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Set<RemittanceData<?>> getRemittances() {
		return remittances;
	}

	public void setRemittances(Set<RemittanceData<?>> remittances) {
		this.remittances = remittances;
	}

	public PersonNameData getCreator() {
		return creator;
	}

	public void setCreator(PersonNameData creator) {
		PersonNameData oldValue = this.creator;
		this.creator = creator;
		pcs.firePropertyChange(PROPERTYNAME_CREATOR, oldValue, creator);
	}

	public String getInvoiceText() {
		return invoiceText;
	}

	public void setInvoiceText(String invoiceText) {
		String oldValue = this.invoiceText;
		this.invoiceText = invoiceText;
		pcs.firePropertyChange(PROPERTYNAME_INVOICETEXT, oldValue, invoiceText);
	}

	public RemittanceType getRemittanceType() {
		return remittanceType;
	}

	public void setRemittanceType(RemittanceType remittanceType) {
		RemittanceType oldValue = this.remittanceType;
		this.remittanceType = remittanceType;
		pcs.firePropertyChange(PROPERTYNAME_REMITTANCETYPE, oldValue,
				remittanceType);

	}

	public TransportType getTransportType() {
		return transportType;
	}

	public void setTransportType(TransportType transportType) {
		TransportType oldValue = this.transportType;
		this.transportType = transportType;
		pcs.firePropertyChange(PROPERTYNAME_TRANSPORTTYPE, oldValue, transportType);
	}

	public Boolean getAddVAT() {
		return addVAT;
	}

	public void setAddVAT(Boolean addVAT) {
		this.addVAT = addVAT;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((csymbol == null) ? 0 : csymbol.hashCode());
		result = prime * result
				+ ((customer == null) ? 0 : customer.hashCode());
		result = prime
				* result
				+ ((incomingInvoiceType == null) ? 0 : incomingInvoiceType
						.hashCode());
		result = prime * result
				+ ((invoiceId == null) ? 0 : invoiceId.hashCode());
		result = prime * result + ((paid == null) ? 0 : paid.hashCode());
		result = prime * result
				+ ((paybackDate == null) ? 0 : paybackDate.hashCode());
		result = prime * result + ((prepaid == null) ? 0 : prepaid.hashCode());
		result = prime * result + ((ssymbol == null) ? 0 : ssymbol.hashCode());
		result = prime * result + ((taxDate == null) ? 0 : taxDate.hashCode());
		result = prime * result + ((vsymbol == null) ? 0 : vsymbol.hashCode());
		result = prime * result + ((pennyBalance == null) ? 0 : pennyBalance.hashCode());
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
		final InvoiceBase<?> other = (InvoiceBase<?>) obj;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (csymbol == null) {
			if (other.csymbol != null)
				return false;
		} else if (!csymbol.equals(other.csymbol))
			return false;
		if (customer == null) {
			if (other.customer != null)
				return false;
		} else if (!customer.equals(other.customer))
			return false;
		if (incomingInvoiceType == null) {
			if (other.incomingInvoiceType != null)
				return false;
		} else if (!incomingInvoiceType.equals(other.incomingInvoiceType))
			return false;
		if (invoiceId == null) {
			if (other.invoiceId != null)
				return false;
		} else if (!invoiceId.equals(other.invoiceId))
			return false;
		if (paid == null) {
			if (other.paid != null)
				return false;
		} else if (!paid.equals(other.paid))
			return false;
		if (paybackDate == null) {
			if (other.paybackDate != null)
				return false;
		} else if (!paybackDate.equals(other.paybackDate))
			return false;
		if (prepaid == null) {
			if (other.prepaid != null)
				return false;
		} else if (!prepaid.equals(other.prepaid))
			return false;
		if (ssymbol == null) {
			if (other.ssymbol != null)
				return false;
		} else if (!ssymbol.equals(other.ssymbol))
			return false;
		if (taxDate == null) {
			if (other.taxDate != null)
				return false;
		} else if (!taxDate.equals(other.taxDate))
			return false;
		if (vsymbol == null) {
			if (other.vsymbol != null)
				return false;
		} else if (!vsymbol.equals(other.vsymbol))
			return false;
		if (pennyBalance == null) {
			if (other.pennyBalance != null)
				return false;
		} else if (!pennyBalance.equals(other.pennyBalance))
			return false;
		return true;
	}

}
