package sk.seges.corpis.server.domain.invoice.base;

import java.beans.PropertyChangeSupport;
import java.util.Date;

import sk.seges.corpis.server.domain.invoice.PriceData;
import sk.seges.corpis.shared.domain.invoice.api.InvoiceData;
import sk.seges.corpis.shared.domain.invoice.api.RemittanceData;

@SuppressWarnings("serial")
public class RemittanceBase<T> implements RemittanceData<T> {

	transient protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	private T id;

	private Date dateReceived;

	private PriceData price;

	private String name;

	private InvoiceData<?> invoice;

	private Boolean prepaid;

	public Date getDateReceived() {
		return dateReceived;
	}

	public void setDateReceived(Date dateReceived) {
		Date oldValue = this.dateReceived;
		this.dateReceived = dateReceived;
		pcs.firePropertyChange(PROPERTY_DATERECEIVED, oldValue, dateReceived);
	}

	@Override
	public T getId() {
		return id;
	}

	@Override
	public void setId(T id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		String oldValue = this.name;
		this.name = name;
		pcs.firePropertyChange(PROPERTY_NAME, oldValue, name);
	}

	@Override
	public PriceData getPrice() {
		return price;
	}

	@Override
	public void setPrice(PriceData price) {
		PriceData oldValue = this.price;
		this.price = price;
		pcs.firePropertyChange(PROPERTY_PRICE, oldValue, price);
	}

	@Override
	public InvoiceData<?> getInvoice() {
		return invoice;
	}

	@Override
	public void setInvoice(InvoiceData<?> invoice) {
		this.invoice = invoice;
	}

	@Override
	public Boolean getPrepaid() {
		return prepaid;
	}

	@Override
	public void setPrepaid(Boolean prepaid) {
		this.prepaid = prepaid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dateReceived == null) ? 0 : dateReceived.hashCode());
		result = prime * result + ((invoice == null) ? 0 : invoice.hashCode());
		result = prime * result + ((prepaid == null) ? 0 : prepaid.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
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
		final RemittanceBase<?> other = (RemittanceBase<?>) obj;
		if (dateReceived == null) {
			if (other.dateReceived != null)
				return false;
		} else if (!dateReceived.equals(other.dateReceived))
			return false;
		if (invoice == null) {
			if (other.invoice != null)
				return false;
		} else if (!invoice.equals(other.invoice))
			return false;
		if (prepaid == null) {
			if (other.prepaid != null)
				return false;
		} else if (!prepaid.equals(other.prepaid))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		return true;
	}

}
