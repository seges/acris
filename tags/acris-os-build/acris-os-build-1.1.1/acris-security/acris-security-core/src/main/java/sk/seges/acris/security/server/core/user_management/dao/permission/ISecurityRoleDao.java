package sk.seges.acris.security.server.core.user_management.dao.permission;

import java.util.List;

import sk.seges.acris.security.shared.user_management.domain.api.RoleData;
import sk.seges.sesam.dao.ICrudDAO;

public interface ISecurityRoleDao<T extends RoleData> extends ICrudDAO<T> {

	public List<String> findSelectedPermissions(Integer roleId);

	public RoleData findByName(String name);
}