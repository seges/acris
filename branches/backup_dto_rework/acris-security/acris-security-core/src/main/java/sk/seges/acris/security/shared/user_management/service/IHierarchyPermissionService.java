package sk.seges.acris.security.shared.user_management.service;

import java.util.List;

import sk.seges.acris.security.shared.user_management.domain.api.HierarchyPermission;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

import com.google.gwt.user.client.rpc.RemoteService;

public interface IHierarchyPermissionService extends RemoteService {

	public List<HierarchyPermission> findSecurityPermissions(Integer parentId, String webId);

	public PagedResult<List<HierarchyPermission>> findAll(String webId, Page page);
}
