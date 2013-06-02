package sk.seges.corpis.server.domain.invoice;

import java.util.Date;
import java.util.Set;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.HasVersion;
import sk.seges.corpis.server.domain.PersonCore;
import sk.seges.corpis.server.domain.customer.CustomerCore;
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
	CustomerCore customer();
	String csymbol();
	String ssymbol();
	String vsymbol();
	Boolean paid();
	Boolean prepaid();
	Boolean incomingInvoiceType();
	Set<InvoiceItem> invoiceItems();
	Double pennyBalance();
	Set<Remittance> remittances();
	PersonCore creator();
	String invoiceText();
	Double finalPrice();
	RemittanceType remittanceType();
	TransportType transportType();
	Boolean addVAT();
}
