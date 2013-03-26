package sk.seges.corpis.server.domain;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.sesam.domain.IDomainObject;

@DomainInterface
@BaseObject
public interface Name extends IDomainObject<Long> {

	String language();
	
	String value();

}