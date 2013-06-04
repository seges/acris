package sk.seges.acris.security.rpc.user_management.service;

import java.util.List;

import sk.seges.acris.security.rpc.user_management.domain.SecurityRole;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ISecurityRoleServiceAsync {
    public void findRole(String roleName, AsyncCallback<SecurityRole> callback);
    public void findSelectedPermissions(Integer roleId, AsyncCallback<List<String>> callback);
    public void findAll(Page page, AsyncCallback<PagedResult<List<SecurityRole>>> callback);
    public void persist(SecurityRole role, AsyncCallback<SecurityRole> callback);
    public void merge(SecurityRole role, AsyncCallback<SecurityRole> callback);
    public void remove(SecurityRole role, AsyncCallback<Void> callback);
}
