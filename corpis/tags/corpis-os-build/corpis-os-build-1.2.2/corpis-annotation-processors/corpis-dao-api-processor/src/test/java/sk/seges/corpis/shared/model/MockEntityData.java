package sk.seges.corpis.shared.model;

import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject;
import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject.Provider;
import sk.seges.sesam.domain.IDomainObject;

@DataAccessObject(provider = Provider.INTERFACE)
public interface MockEntityData extends IDomainObject {

	String getName();
	
	void setName(String name);
}