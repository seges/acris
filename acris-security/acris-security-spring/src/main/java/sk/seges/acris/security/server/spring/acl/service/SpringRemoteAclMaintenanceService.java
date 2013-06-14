package sk.seges.acris.security.server.spring.acl.service;

import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.server.acl.service.AclMaintenanceService;
import sk.seges.acris.security.server.acl.service.api.AclManager;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;
import sk.seges.sesam.security.server.model.acl.AclDataRegistry;
import sk.seges.sesam.shared.model.converter.ConverterProviderContext;

public class SpringRemoteAclMaintenanceService extends AclMaintenanceService {

	public SpringRemoteAclMaintenanceService(ConverterProviderContext converterProviderContext, AclManager aclManager, AclDataRegistry aclDataRegistry) {
		super(converterProviderContext, aclManager, aclDataRegistry);
	}

	@Transactional
	public void removeACLEntries(UserData user, String[] securedClassNames) {
		super.removeACLEntries(user, securedClassNames);
	}
}