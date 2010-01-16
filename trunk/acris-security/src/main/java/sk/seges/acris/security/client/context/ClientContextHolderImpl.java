package sk.seges.acris.security.client.context;

import sk.seges.acris.security.rpc.to.ClientContext;
import sk.seges.acris.security.rpc.to.ClientContextHolder;

public class ClientContextHolderImpl implements ClientContextHolder {

	private static ClientContext clientContext;

	public ClientContext getClientContext() {
		return clientContext;
	}

	public void setClientContext(ClientContext clientContext) {
		ClientContextHolderImpl.clientContext = clientContext;
	}
}
