package sk.seges.corpis.server.domain.search;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.shared.domain.api.HasCodeListField;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
public interface SupValue extends IMutableDomainObject<Long>, HasCodeListField {

	Sup sup();
	String locale();
	String sValue();	
	SupIndex supIndex();
}
