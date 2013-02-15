package sk.seges.corpis.server.domain;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
public interface Country extends IMutableDomainObject<Long> {

	String label();
	Boolean europeanUnion();
	String domain();
	String country();
	String language();

}