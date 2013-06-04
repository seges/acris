package sk.seges.corpis.server.domain.search;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.HasWebId;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
public interface Sup extends IMutableDomainObject<Long>, HasWebId {

	Sup parentSup();
	String names();
	String type();
	String classType();

}