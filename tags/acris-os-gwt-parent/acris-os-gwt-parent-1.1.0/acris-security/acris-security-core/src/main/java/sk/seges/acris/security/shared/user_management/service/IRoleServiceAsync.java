package sk.seges.acris.security.shared.user_management.service;

import java.util.List;

import sk.seges.acris.security.shared.user_management.domain.api.RoleData;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IRoleServiceAsync {

	public void findRole(String roleName, AsyncCallback<RoleData> callback);

	public void findSelectedPermissions(Integer roleId, AsyncCallback<List<String>> callback);

	public void findAll(Page page, AsyncCallback<PagedResult<List<RoleData>>> callback);

	public void persist(RoleData role, AsyncCallback<RoleData> callback);

	public void merge(RoleData role, AsyncCallback<RoleData> callback);

	public void remove(RoleData role, AsyncCallback<Void> callback);
}
