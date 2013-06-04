package sk.seges.corpis.server.domain.invoice.base;

import java.beans.PropertyChangeSupport;
import java.util.Date;

import sk.seges.corpis.shared.domain.invoice.api.VatData;

@SuppressWarnings("serial")
public class VatBase implements VatData<Short> {

	private Short vat;
	private Date validFrom;

	transient protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	@Override
	public Short getVat() {
		return vat;
	}

	@Override
	public void setVat(Short vat) {
		Short oldValue = this.vat;
		this.vat = vat;
		pcs.firePropertyChange(PROPERTY_VAT, oldValue, vat);
	}

	@Override
	public Date getValidFrom() {
		return validFrom;
	}

	@Override
	public void setValidFrom(Date validFrom) {
		Date oldValue = this.validFrom;
		this.validFrom = validFrom;
		pcs.firePropertyChange(PROPERTY_VALIDFROM, oldValue, validFrom);
	}

	@Override
	public Short getId() {
		return getVat();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (!(other instanceof VatBase))
			return false;
		final VatBase that = (VatBase) other;
		if (!this.getVat().equals(that.getVat()))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = 14;
		result = 29 * result + getVat().hashCode();
		return result;
	}
}