package sk.seges.acris.security.shared.user_management.domain.api;

import sk.seges.acris.core.client.rpc.IDataTransferObject;

public interface UserContext extends IDataTransferObject {

	String getWebId();
}
