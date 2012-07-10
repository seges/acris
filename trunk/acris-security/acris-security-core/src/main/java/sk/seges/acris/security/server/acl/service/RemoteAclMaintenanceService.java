package 	sk.seges.acris.security.server.acl.service;

import java.util.List;

import sk.seges.acris.security.server.acl.service.api.AclManager;
import sk.seges.acris.security.server.utils.SecuredClassHelper;
import sk.seges.acris.security.shared.acl.service.IRemoteAclMaintenanceService;
import sk.seges.acris.security.shared.domain.ISecuredObject;
import sk.seges.acris.security.shared.user_management.domain.Permission;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.acris.security.shared.util.LoggedUserRole;
import sk.seges.sesam.server.domain.converter.utils.ClassConverter;

public class RemoteAclMaintenanceService implements IRemoteAclMaintenanceService {

    private AclManager aclManager;
    
    public AclManager getAclManager() {
        return aclManager;
    }

    public void setAclManager(AclManager aclManager) {
        this.aclManager = aclManager;
    }

    public RemoteAclMaintenanceService(AclManager aclManager) {
    	this.aclManager = aclManager;
    }
    
    public void removeACLEntries(UserData<?> user, String[] securedClassNames) {
        for(String securedClassName : securedClassNames) {
            Class<? extends ISecuredObject<?>> securedClass = SecuredClassHelper.getSecuredClass(securedClassName);
            aclManager.removeAclRecords(securedClass, user);
        }
    }
    
    public void removeACLEntries(List<Long> aclIds, String className, UserData<?> user) {
    	className=  ClassConverter.getDomainClassName(className);
    	for (Long id : aclIds) {
    		aclManager.removeAclRecords(id, className, user);
    	}
    }

    public void resetACLEntries(String className, Long aclId, UserData<?> user, Permission[] authorities) {
    	className=  ClassConverter.getDomainClassName(className);
   		aclManager.resetAclRecords(SecuredClassHelper.getSecuredClass(className), aclId, user, authorities);
    }

    @Override
    public void resetACLEntriesLoggedRole(String className, Long aclId, Permission[] authorities) {
    	className=  ClassConverter.getDomainClassName(className);
   		aclManager.resetAclRecords(SecuredClassHelper.getSecuredClass(className), aclId, new LoggedUserRole(), authorities);
    }
}