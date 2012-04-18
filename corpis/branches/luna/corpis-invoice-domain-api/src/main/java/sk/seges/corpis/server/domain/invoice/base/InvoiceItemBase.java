package sk.seges.corpis.server.domain.invoice.base;

import java.beans.PropertyChangeSupport;

import sk.seges.corpis.server.domain.invoice.PriceData;
import sk.seges.corpis.server.domain.invoice.UnitData;
import sk.seges.corpis.server.domain.invoice.VatData;
import sk.seges.corpis.shared.domain.invoice.api.InvoiceData;
import sk.seges.corpis.shared.domain.invoice.api.InvoiceItemData;

@SuppressWarnings("serial")
public class InvoiceItemBase<T> implements InvoiceItemData<T> {

	transient protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	private T id;

	private Integer version;

	private Float count;

	private String description;

	private PriceData price;

	private UnitData<?> unit;

	private VatData<?> vat;

	private InvoiceData<?> invoice;

	@Override
	public InvoiceData<?> getInvoice() {
		return invoice;
	}

	@Override
	public void setInvoice(InvoiceData<?> invoice) {
		InvoiceData<?> oldValue = this.invoice;
		this.invoice = invoice;
		pcs.firePropertyChange(PROPERTYNAME_INVOICE, oldValue, invoice);
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
	public Integer getVersion() {
		return version;
	}

	@Override
	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public Float getCount() {
		return count;
	}

	@Override
	public void setCount(Float count) {
		Float oldValue = this.count;
		this.count = count;
		pcs.firePropertyChange(PROPERTYNAME_COUNT, oldValue, count);
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		String oldValue = this.description;
		this.description = description;
		pcs.firePropertyChange(PROPERTYNAME_DESCRIPTION, oldValue, description);
	}

	@Override
	public PriceData getPrice() {
		return price;
	}

	@Override
	public void setPrice(PriceData price) {
		PriceData oldValue = this.price;
		this.price = price;
		pcs.firePropertyChange(PROPERTYNAME_PRICE, oldValue, price);
	}

	@Override
	public UnitData<?> getUnit() {
		return unit;
	}

	@Override
	public void setUnit(UnitData<?> unit) {
		UnitData<?> oldValue = this.unit;
		this.unit = unit;
		pcs.firePropertyChange(PROPERTYNAME_UNIT, oldValue, unit);
	}

	@Override
	public VatData<?> getVat() {
		return vat;
	}

	@Override
	public void setVat(VatData<?> vat) {
		VatData<?> oldValue = this.vat;
		this.vat = vat;
		pcs.firePropertyChange(PROPERTYNAME_VAT, oldValue, vat);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((count == null) ? 0 : count.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
		result = prime * result + ((vat == null) ? 0 : vat.hashCode());
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
		final InvoiceItemBase<?> other = (InvoiceItemBase<?>) obj;
		if (count == null) {
			if (other.count != null)
				return false;
		} else if (!count.equals(other.count))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (unit == null) {
			if (other.unit != null)
				return false;
		} else if (!unit.equals(other.unit))
			return false;
		if (vat == null) {
			if (other.vat != null)
				return false;
		} else if (!vat.equals(other.vat))
			return false;
		return true;
	}
}