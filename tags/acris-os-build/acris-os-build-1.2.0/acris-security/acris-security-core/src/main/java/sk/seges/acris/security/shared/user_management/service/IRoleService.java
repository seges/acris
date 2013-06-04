package sk.seges.acris.security.shared.user_management.service;

import java.util.List;

import sk.seges.acris.security.shared.user_management.domain.api.RoleData;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

import com.google.gwt.user.client.rpc.RemoteService;

public interface IRoleService extends RemoteService {

	public RoleData findRole(String roleName);

	public List<String> findSelectedPermissions(Integer roleId);

	public PagedResult<List<RoleData>> findAll(Page page);

	public RoleData persist(RoleData role);

	public RoleData merge(RoleData role);

	public void remove(RoleData role);
}
