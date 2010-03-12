package sk.seges.acris.security.server.dao.permission;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import sk.seges.acris.security.rpc.domain.SecurityRole;
import sk.seges.sesam.dao.ICrudDAO;

public interface ISecurityRoleDao extends ICrudDAO<SecurityRole> {
    public SecurityRole findUniqueResultByCriteria(DetachedCriteria criteria);
    public List<String> findSelectedPermissions(Integer roleId);
}
