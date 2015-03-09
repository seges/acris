package sk.seges.acris.security.shared.user_management.domain.api;

import sk.seges.acris.core.shared.model.IDataTransferObject;

public interface UserContext extends IDataTransferObject {

	String getWebId();
	
	String getLocale();
}
