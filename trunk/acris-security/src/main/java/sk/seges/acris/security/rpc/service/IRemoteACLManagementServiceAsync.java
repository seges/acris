package sk.seges.acris.security.rpc.service;

import java.util.List;

import org.springframework.security.userdetails.UserDetails;

import sk.seges.acris.security.rpc.domain.ISecuredObject;
import sk.seges.acris.security.rpc.domain.Permission;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IRemoteACLManagementServiceAsync {
    public void setACLEntries(List<ISecuredObject> securedObjects, UserDetails user, AsyncCallback<Void> callback);
    public void setACLEntries(List<ISecuredObject> securedObjects, UserDetails user, Permission[] authorities, AsyncCallback<Void> callback);
    public void removeACLEntries(UserDetails user, String[] securedClassNames, AsyncCallback<Void> callback);
    public void removeACLEntries(ISecuredObject securedObject, UserDetails user, AsyncCallback<Void> callback);
}
