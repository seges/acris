package sk.seges.acris.security.server.acl.service;

import org.apache.log4j.Logger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.rpc.acl.service.IRemoteAclMaintenanceService;
import sk.seges.acris.security.rpc.domain.ISecuredObject;
import sk.seges.acris.security.rpc.user_management.domain.Permission;

@Service
public class RemoteAclMaintenanceService implements IRemoteAclMaintenanceService {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(RemoteAclMaintenanceService.class);

    @Autowired
    private AclMaintainer aclManager;
    
    public AclMaintainer getAclManager() {
        return aclManager;
    }

    public void setAclManager(AclMaintainer aclManager) {
        this.aclManager = aclManager;
    }

    @Transactional
    public void removeACLEntries(UserDetails user, String[] securedClassNames) {
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
    
    @Transactional
    public void removeACLEntries(List<? extends ISecuredObject> securedObjects, UserDetails user) {
        for(ISecuredObject securedObject : securedObjects) {
            aclManager.removeAclRecords(securedObject, user);
        }
    }

    @Transactional
    public void setACLEntries(List<? extends ISecuredObject> securedObjects, UserDetails user, Permission[] authorities) {
        for(ISecuredObject securedObject : securedObjects) {
            aclManager.setAclRecords(securedObject, user, authorities);
        }
    }

    @Transactional
    public void setACLEntries(List<? extends ISecuredObject> securedObjects, UserDetails user) {
        setACLEntries(securedObjects, user, new Permission[] {
                Permission.VIEW,
                Permission.CREATE,
                Permission.EDIT,
                Permission.DELETE
        });
    }
}
