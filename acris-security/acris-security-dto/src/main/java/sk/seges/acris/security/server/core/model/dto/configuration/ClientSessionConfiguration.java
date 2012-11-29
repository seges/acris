package sk.seges.acris.security.server.core.model.dto.configuration;

import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.sesam.pap.model.annotation.Ignore;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

@TransferObjectMapping(domainClass = ClientSession.class, dtoClass = ClientSession.class)
public interface ClientSessionConfiguration {
	
	@Ignore
	void session();

}