package sk.seges.corpis.shared.model;

import sk.seges.sesam.domain.IDomainObject;

public interface PlainMockEntityData<T> extends IDomainObject<T> {

	String getName();
	
	void setName(String name);
}