package sk.seges.acris.security.rpc.to;


public interface ClientContextHolder {

	/**
	 * @return ClientContext
	 */
	ClientContext getClientContext();

	/**
	 * @param clientContext
	 */
	void setClientContext(ClientContext clientContext);
}
