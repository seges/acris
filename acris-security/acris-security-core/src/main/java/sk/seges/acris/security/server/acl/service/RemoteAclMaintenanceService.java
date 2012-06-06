package 	sk.seges.acris.security.server.acl.service;

import java.util.List;

import org.apache.log4j.Logger;

import sk.seges.acris.security.server.acl.service.api.AclManager;
import sk.seges.acris.security.shared.acl.service.IRemoteAclMaintenanceService;
import sk.seges.acris.security.shared.user_management.domain.Permission;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.acris.security.shared.util.LoggedUserRole;

public class RemoteAclMaintenanceService implements IRemoteAclMaintenanceService {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(RemoteAclMaintenanceService.class);

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
    
    public void removeACLEntries(UserData user, String[] securedClassNames) {
        for(String securedClassName : securedClassNames) {
            Class securedClass = null;
            try {
                securedClass = Class.forName(securedClassName);
            } catch (ClassNotFoundException e) {
                String message = "Class " + securedClassName + " could not be found.";
                logger.error(message, e);
                throw new RuntimeException(message);
            }
            aclManager.removeAclRecords(securedClass, user);
        }
    }
    
    public void removeACLEntries(List<Long> aclIds, String className, UserData user) {
    	for (Long id : aclIds) {
    		aclManager.removeAclRecords(id, className, user);
    	}
    }

    public void resetACLEntries(Long aclId, UserData user, Permission[] authorities) {
   		aclManager.resetAclRecords(aclId, user, authorities);
    }

    @Override
    public void resetACLEntriesLoggedRole(Long aclId, Permission[] authorities) {
    	LoggedUserRole role = new LoggedUserRole();
   		aclManager.resetAclRecords(aclId, role, authorities);
    }
}
