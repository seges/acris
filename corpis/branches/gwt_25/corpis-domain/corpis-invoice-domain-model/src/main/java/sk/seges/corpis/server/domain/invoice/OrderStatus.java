package sk.seges.corpis.server.domain.invoice;

import java.util.List;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.HasWebId;
import sk.seges.corpis.server.domain.Name;
import sk.seges.corpis.shared.domain.invoice.EOrderStatus;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
public interface OrderStatus extends IMutableDomainObject<Long>, HasWebId{

	String webId();
	List<Name> names();
	Integer index();
	EOrderStatus type();
}
