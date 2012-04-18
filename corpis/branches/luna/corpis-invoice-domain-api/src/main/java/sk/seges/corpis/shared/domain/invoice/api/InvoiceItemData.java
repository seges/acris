package sk.seges.corpis.shared.domain.invoice.api;

import sk.seges.corpis.server.domain.invoice.HasPrice;
import sk.seges.corpis.server.domain.invoice.UnitData;
import sk.seges.corpis.server.domain.invoice.VatData;
import sk.seges.corpis.shared.domain.api.HasDescription;
import sk.seges.corpis.shared.domain.api.HasVersion;
import sk.seges.sesam.domain.IMutableDomainObject;

public interface InvoiceItemData<T> extends IMutableDomainObject<T>, HasVersion, HasDescription, HasPrice {

	public static final String PROPERTYNAME_INVOICE = "invoice"; //$NON-NLS-1$
	public static final String PROPERTYNAME_COUNT = "count"; //$NON-NLS-1$
	public static final String PROPERTYNAME_DESCRIPTION = "description"; //$NON-NLS-1$
	public static final String PROPERTYNAME_PRICE = "price"; //$NON-NLS-1$
	public static final String PROPERTYNAME_CURRENCY = "currency"; //$NON-NLS-1$
	public static final String PROPERTYNAME_UNIT = "unit"; //$NON-NLS-1$
	public static final String PROPERTYNAME_VAT = "vat"; //$NON-NLS-1$

	InvoiceData<?> getInvoice();

	void setInvoice(InvoiceData<?> invoice);

	Float getCount();

	void setCount(Float count);

	UnitData<?> getUnit();

	void setUnit(UnitData<?> unit);

	VatData<?> getVat();

	void setVat(VatData<?> vat);
}