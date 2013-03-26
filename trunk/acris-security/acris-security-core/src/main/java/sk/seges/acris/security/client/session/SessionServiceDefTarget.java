package sk.seges.acris.security.client.session;

import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.acris.security.shared.user_management.model.dto.GenericUserDTO;

import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface SessionServiceDefTarget extends ServiceDefTarget {

	ClientSession<GenericUserDTO> getSession();
	
	void setSession(ClientSession<? extends GenericUserDTO> clientSession);
}
