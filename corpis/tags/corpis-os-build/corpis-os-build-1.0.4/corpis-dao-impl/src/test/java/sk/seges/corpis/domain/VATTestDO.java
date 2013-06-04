package sk.seges.corpis.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import sk.seges.sesam.domain.IDomainObject;

@Entity
@Table(name = "vats")
public class VATTestDO implements IDomainObject<Short> {
	private static final long serialVersionUID = -598285433434368847L;
	@Id
	private Short vat;
	@Temporal(TemporalType.DATE)
	private Date validFrom;
	
	@Override
	public Short getId() {
		return vat;
	}

	@Deprecated
	/*
	 * Use getId instead
	 */
	public Short getVat() {
		return vat;
	}
	//TODO rename to setId
	public void setVat(Short vat) {
		this.vat = vat;
	}
	public Date getValidFrom() {
		return validFrom;
	}
	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}
	@Override
	public String toString() {
		return "VAT [validFrom=" + validFrom + ", vat=" + vat + "]";
	}
}
