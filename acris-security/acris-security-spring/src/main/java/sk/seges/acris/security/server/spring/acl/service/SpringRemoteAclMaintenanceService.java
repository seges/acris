package sk.seges.acris.security.server.spring.acl.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.server.acl.service.AclMaintenanceService;
import sk.seges.acris.security.server.acl.service.api.AclManager;
import sk.seges.acris.security.user_management.server.model.data.UserData;
import sk.seges.sesam.shared.model.converter.ConverterProviderContext;

public class SpringRemoteAclMaintenanceService extends AclMaintenanceService {

	public SpringRemoteAclMaintenanceService(AclManager aclManager) {
		super(null, aclManager);
	}
	
	public SpringRemoteAclMaintenanceService(ConverterProviderContext converterProviderContext, AclManager aclManager) {
		super(converterProviderContext, aclManager);
	}

	@Transactional
	public void removeACLEntries(UserData user, String[] securedClassNames) {
		super.removeACLEntries(user, securedClassNames);
	}

	@Transactional
	public void removeACLEntries(List<Long> aclIds, String className, UserData user) {
		super.removeACLEntries(aclIds, className, user);
	}
}