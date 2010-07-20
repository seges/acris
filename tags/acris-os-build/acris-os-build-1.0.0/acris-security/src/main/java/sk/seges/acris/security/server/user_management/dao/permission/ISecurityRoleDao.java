package sk.seges.acris.security.server.user_management.dao.permission;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import sk.seges.acris.security.rpc.user_management.domain.SecurityRole;
import sk.seges.sesam.dao.ICrudDAO;

public interface ISecurityRoleDao extends ICrudDAO<SecurityRole> {
	public SecurityRole findUniqueResultByCriteria(DetachedCriteria criteria);

	public List<String> findSelectedPermissions(Integer roleId);

	public SecurityRole findByName(String name);
}