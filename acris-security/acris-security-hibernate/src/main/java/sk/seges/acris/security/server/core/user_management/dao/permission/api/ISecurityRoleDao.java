package sk.seges.acris.security.server.core.user_management.dao.permission.api;

import java.util.List;

import sk.seges.corpis.server.domain.user.server.model.data.RoleData;
import sk.seges.sesam.dao.ICrudDAO;

public interface ISecurityRoleDao<T extends RoleData> extends ICrudDAO<T> {

	public List<String> findSelectedPermissions(Integer roleId);

	public RoleData findByName(String name, String webId);
}