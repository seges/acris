package sk.seges.corpis.server.domain.invoice;

import java.io.Serializable;
import java.util.List;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.HasDescription;
import sk.seges.corpis.server.domain.HasPrice;
import sk.seges.corpis.server.domain.Price;
import sk.seges.corpis.server.domain.Vat;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
public interface AccountableItem extends HasPrice, HasDescription, Serializable, IMutableDomainObject<Long> {

	Price basePrice();

	String extId();

	Float amount();
	Unit unit();
	Vat vat();
	Float weight();

	List<InvoiceItem> invoiceItems();
}