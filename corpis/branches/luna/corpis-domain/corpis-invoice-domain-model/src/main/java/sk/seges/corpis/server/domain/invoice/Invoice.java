package sk.seges.corpis.server.domain.invoice;

import java.util.Date;
import java.util.Set;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.HasVersion;
import sk.seges.corpis.server.domain.PersonName;
import sk.seges.corpis.server.domain.customer.Customer;
import sk.seges.corpis.shared.domain.invoice.RemittanceType;
import sk.seges.corpis.shared.domain.invoice.TransportType;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
public interface Invoice extends IMutableDomainObject<Long>, HasVersion {

	Date taxDate();
	Date paybackDate();
	Date creationDate();
	Integer invoiceId();
	Customer customer();
	String csymbol();
	String ssymbol();
	String vsymbol();
	Boolean paid();
	Boolean prepaid();
	Boolean incomingInvoiceType();
	Set<InvoiceItem> invoiceItems();
	Double pennyBalance();
	Set<Remittance> remittances();
	PersonName creator();
	String invoiceText();
	Double finalPrice();
	RemittanceType remittanceType();
	TransportType transportType();
	Boolean addVAT();
}
