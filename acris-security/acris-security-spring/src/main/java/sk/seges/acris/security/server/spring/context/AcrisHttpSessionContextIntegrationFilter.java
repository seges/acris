package sk.seges.acris.security.server.spring.context;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;


public class AcrisHttpSessionContextIntegrationFilter extends HttpSessionSecurityContextRepository {

	@Override
	protected SecurityContext generateNewContext() {
		return new AcrisSecurityContext();
	}
}
