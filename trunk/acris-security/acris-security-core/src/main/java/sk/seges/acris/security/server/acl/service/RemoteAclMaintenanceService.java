package sk.seges.acris.security.server.acl.service;

import java.util.List;

import org.apache.log4j.Logger;

import sk.seges.acris.security.server.acl.service.api.AclManager;
import sk.seges.acris.security.shared.acl.service.IRemoteAclMaintenanceService;
import sk.seges.acris.security.shared.domain.ISecuredObject;
import sk.seges.acris.security.shared.user_management.domain.Permission;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;

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
    
    public void removeACLEntries(List<? extends ISecuredObject> securedObjects, UserData user) {
        for(ISecuredObject securedObject : securedObjects) {
            aclManager.removeAclRecords(securedObject, user);
        }
    }

    public void setACLEntries(List<? extends ISecuredObject> securedObjects, UserData user, Permission[] authorities) {
        for(ISecuredObject securedObject : securedObjects) {
            aclManager.setAclRecords(securedObject, user, authorities);
        }
    }

    public void setACLEntries(List<? extends ISecuredObject> securedObjects, UserData user) {
        setACLEntries(securedObjects, user, new Permission[] {
                Permission.VIEW,
                Permission.CREATE,
                Permission.EDIT,
                Permission.DELETE
        });
    }
}
