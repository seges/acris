package sk.seges.acris.security.server.service;

import org.apache.log4j.Logger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.rpc.domain.ISecuredObject;
import sk.seges.acris.security.rpc.domain.Permission;
import sk.seges.acris.security.rpc.service.IRemoteACLManagementService;

@Service
public class RemoteACLManagementService implements IRemoteACLManagementService {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(RemoteACLManagementService.class);

    @Autowired
    private ACLManager aclManager;
    
    public ACLManager getAclManager() {
        return aclManager;
    }

    public void setAclManager(ACLManager aclManager) {
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
            aclManager.removeACLEntries(securedClass, user);
        }
    }
    
    @Transactional
    public void removeACLEntries(ISecuredObject securedObject, UserDetails user) {
        aclManager.removeACLEntries(securedObject, user);
    }

    @Transactional
    public void setACLEntries(List<? extends ISecuredObject> securedObjects, UserDetails user, Permission[] authorities) {
        for(ISecuredObject securedObject : securedObjects) {
            aclManager.setACLEntries(securedObject, user, authorities);
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
