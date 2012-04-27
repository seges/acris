package sk.seges.corpis.server.domain.invoice;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.HasDescription;
import sk.seges.corpis.server.domain.HasVersion;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
public interface InvoiceItem extends IMutableDomainObject<Long>, HasVersion, HasDescription, HasPrice {

	Invoice invoice();
	Float count();
	Unit unit();
	Vat vat();
}
