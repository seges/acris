package sk.seges.corpis.shared.model.mock.api;

import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject;
import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject.Provider;
import sk.seges.sesam.domain.IDomainObject;

@DataAccessObject(provider = Provider.INTERFACE)
public interface MockEntity extends IDomainObject<Long> {

	String getName();
	
	void setName(String name);
}