package sk.seges.corpis.shared.domain.invoice.api;

import java.util.Date;

import sk.seges.corpis.server.domain.invoice.HasPrice;
import sk.seges.corpis.shared.domain.api.HasName;
import sk.seges.sesam.domain.IMutableDomainObject;

public interface RemittanceData<T> extends IMutableDomainObject<T>, HasName, HasPrice {

	public static final String PROPERTY_DATERECEIVED = "dateReceived"; //$NON-NLS-1$
	public static final String PROPERTY_PRICE = "price"; //$NON-NLS-1$
	public static final String PROPERTY_CURRENCY = "currency"; //$NON-NLS-1$
	public static final String PROPERTY_NAME = "name"; //$NON-NLS-1$
	public static final String PROPERTY_PREPAID = "prepaid"; //$NON-NLS-1$

	Date getDateReceived();

	void setDateReceived(Date dateReceived);

	InvoiceData<?> getInvoice();

	void setInvoice(InvoiceData<?> invoice);

	Boolean getPrepaid();

	void setPrepaid(Boolean prepaid);
}