package sk.seges.corpis.shared.model.mock;

import sk.seges.sesam.domain.IDomainObject;

public interface MockEntity extends IDomainObject<Long> {

	String getName();
	
	void setName(String name);
}