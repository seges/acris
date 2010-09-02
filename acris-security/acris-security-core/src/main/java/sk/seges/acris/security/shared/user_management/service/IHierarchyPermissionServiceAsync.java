package sk.seges.acris.security.shared.user_management.service;

import java.util.List;

import sk.seges.acris.security.shared.user_management.domain.api.HierarchyPermission;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IHierarchyPermissionServiceAsync {

	public void findAll(String webId, Page page, AsyncCallback<PagedResult<List<HierarchyPermission>>> callback);

	public void findSecurityPermissions(Integer parentId, String webId, AsyncCallback<List<HierarchyPermission>> callback);
}
