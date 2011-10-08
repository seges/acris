package sk.seges.corpis.shared.domain.invoice.api;

import java.util.Date;

import sk.seges.sesam.domain.IDomainObject;

public interface VatData<T> extends IDomainObject<T> {

	public static final String PROPERTY_VAT = "vat"; //$NON-NLS-1$
	public static final String PROPERTY_VALIDFROM = "validFrom"; //$NON-NLS-1$

	Short getVat();

	void setVat(Short vat);

	Date getValidFrom();

	void setValidFrom(Date validFrom);

}
