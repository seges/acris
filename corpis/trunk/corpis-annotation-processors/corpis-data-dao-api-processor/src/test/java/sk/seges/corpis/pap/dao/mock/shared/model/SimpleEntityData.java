package sk.seges.corpis.pap.dao.mock.shared.model;

import sk.seges.sesam.domain.IDomainObject;

public interface SimpleEntityData<T> extends IDomainObject<T> {

	void setNumber(Long number);
	Long getNumber();
	
	void setTest(String test);
	String getTest();
	
	void setType(T type);
	T getType();

}
