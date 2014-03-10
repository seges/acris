package sk.seges.acris.security.client.session;

import com.google.gwt.user.client.rpc.ServiceDefTarget;
import sk.seges.acris.security.shared.session.ClientSessionDTO;

public interface SessionServiceDefTarget extends ServiceDefTarget {

	ClientSessionDTO getSession();
	
	void setSession(ClientSessionDTO clientSession);
}
