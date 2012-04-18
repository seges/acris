package sk.seges.corpis.server.domain;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
public interface BasicContact extends IMutableDomainObject<Long>{

	String phone();
	String fax();
	String email();
	String mobile();
	String web();

}