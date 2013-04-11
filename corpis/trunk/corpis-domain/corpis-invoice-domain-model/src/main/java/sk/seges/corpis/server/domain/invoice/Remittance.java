package sk.seges.corpis.server.domain.invoice;

import java.util.Date;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.HasName;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
public interface Remittance extends IMutableDomainObject<Long>, HasName, HasPrice {

	Date dateReceived();
	Invoice invoice();
	Boolean prepaid();
}