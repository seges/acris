package sk.seges.corpis.shared.model.mock.api;

import sk.seges.sesam.domain.IDomainObject;

public interface MockEntityData extends IDomainObject<Long> {

	String getName();
	
	void setName(String name);
}