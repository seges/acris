package sk.seges.acris.security.server.core.user_management.dao.permission.hibernate;

import sk.seges.acris.security.server.core.user_management.dao.permission.api.ISecurityRoleDao;
import sk.seges.acris.security.user_management.server.model.data.RoleData;
import sk.seges.corpis.dao.hibernate.IHibernateFinderDAO;

public interface IHibernateSecurityRoleDao<T extends RoleData> extends ISecurityRoleDao<T>, IHibernateFinderDAO<T> {	
}