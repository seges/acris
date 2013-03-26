package sk.seges.corpis.server.domain.news;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
public interface NewsItemPK extends IMutableDomainObject<Long> {

	String language();

	String category();

}