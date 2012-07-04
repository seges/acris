package sk.seges.corpis.server.domain.invoice;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
public interface ProductPrice extends IMutableDomainObject<Long> {

	Price price();

	Short priority();

}
