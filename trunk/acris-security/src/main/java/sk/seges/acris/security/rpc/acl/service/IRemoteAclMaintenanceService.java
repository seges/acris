package sk.seges.acris.security.rpc.acl.service;

import java.util.List;

import org.springframework.security.userdetails.UserDetails;

import sk.seges.acris.security.rpc.domain.ISecuredObject;
import sk.seges.acris.security.rpc.user_management.domain.Permission;

import com.google.gwt.user.client.rpc.RemoteService;

public interface IRemoteAclMaintenanceService extends RemoteService {
    public void setACLEntries(List<? extends ISecuredObject> securedObjects, UserDetails user);
    public void setACLEntries(List<? extends ISecuredObject> securedObjects, UserDetails user, Permission[] authorities);
    public void removeACLEntries(UserDetails user, String[] securedClassNames);
    public void removeACLEntries(List<? extends ISecuredObject> securedObject, UserDetails user);
}
