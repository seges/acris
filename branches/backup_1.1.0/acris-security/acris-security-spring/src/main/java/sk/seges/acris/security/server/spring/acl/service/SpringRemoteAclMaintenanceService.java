package sk.seges.acris.security.server.spring.acl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.server.acl.service.RemoteAclMaintenanceService;
import sk.seges.acris.security.server.acl.service.api.AclManager;
import sk.seges.acris.security.shared.domain.ISecuredObject;
import sk.seges.acris.security.shared.user_management.domain.Permission;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;

public class SpringRemoteAclMaintenanceService extends RemoteAclMaintenanceService {

	@Autowired
	public SpringRemoteAclMaintenanceService(AclManager aclManager) {
		super(aclManager);
	}

	@Transactional
	public void removeACLEntries(UserData user, String[] securedClassNames) {
		super.removeACLEntries(user, securedClassNames);
	}

	@Transactional
	public void removeACLEntries(List<? extends ISecuredObject> securedObjects, UserData user) {
		super.removeACLEntries(securedObjects, user);
	}

	@Transactional
	public void setACLEntries(List<? extends ISecuredObject> securedObjects, UserData user, Permission[] authorities) {
		super.setACLEntries(securedObjects, user, authorities);
	}

	@Transactional
	public void setACLEntries(List<? extends ISecuredObject> securedObjects, UserData user) {
		super.setACLEntries(securedObjects, user);
	}
}