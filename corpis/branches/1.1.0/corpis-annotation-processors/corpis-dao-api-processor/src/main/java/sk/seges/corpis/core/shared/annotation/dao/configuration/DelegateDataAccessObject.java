package sk.seges.corpis.core.shared.annotation.dao.configuration;

import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject;

public @interface DelegateDataAccessObject {
	DataAccessObject value();
}
