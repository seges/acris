package sk.seges.acris.security.server.core.user_management.dao.permission.hibernate;

import sk.seges.acris.security.server.core.user_management.dao.permission.ISecurityRoleDao;
import sk.seges.acris.security.shared.user_management.domain.api.RoleData;
import sk.seges.corpis.dao.hibernate.IHibernateFinderDAO;

public interface IHibernateSecurityRoleDao<T extends RoleData> extends ISecurityRoleDao<T>, IHibernateFinderDAO<T> {	
}