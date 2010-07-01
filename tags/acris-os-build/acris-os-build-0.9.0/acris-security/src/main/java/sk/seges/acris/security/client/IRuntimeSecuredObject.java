package sk.seges.acris.security.client;

import sk.seges.acris.security.rpc.session.ClientSession;

public interface IRuntimeSecuredObject {
	void setClientSession(ClientSession clientSession);

	ClientSession getClientSession();
}