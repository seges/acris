package sk.seges.corpis.pap.dao.mock.shared.model;

import sk.seges.sesam.domain.IDomainObject;

public interface MockEntityData<T> extends IDomainObject<T> {

	String getType();

	void setType(String type);
}
