package sk.seges.corpis.pap.dao.mock;

import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject;
import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject.Provider;

@DataAccessObject(provider = Provider.INTERFACE)
public interface SimpleEntity<T> {
	
	Long number();
	String test();
	T type();

}