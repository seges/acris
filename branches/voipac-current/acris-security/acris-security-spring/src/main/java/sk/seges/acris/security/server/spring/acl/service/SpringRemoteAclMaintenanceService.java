package sk.seges.acris.security.server.spring.acl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.server.acl.service.RemoteAclMaintenanceService;
import sk.seges.acris.security.server.acl.service.api.AclManager;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.sesam.shared.model.converter.api.ConverterProvider;

public class SpringRemoteAclMaintenanceService extends RemoteAclMaintenanceService {

	@Autowired
	public SpringRemoteAclMaintenanceService(ConverterProvider converterProvider, AclManager aclManager) {
		super(converterProvider, aclManager);
	}

	@Transactional
	public void removeACLEntries(UserData<?> user, String[] securedClassNames) {
		super.removeACLEntries(user, securedClassNames);
	}

	@Transactional
	public void removeACLEntries(List<Long> aclIds, String className, UserData<?> user) {
		super.removeACLEntries(aclIds, className, user);
	}
}