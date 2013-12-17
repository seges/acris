package sk.seges.acris.security.server.core.user_management.dao.permission.hibernate;

import sk.seges.acris.security.server.core.user_management.dao.permission.api.ISecurityRoleDao;
import sk.seges.corpis.dao.hibernate.IHibernateFinderDAO;
import sk.seges.corpis.server.domain.user.server.model.data.RoleData;

public interface IHibernateSecurityRoleDao<T extends RoleData> extends ISecurityRoleDao<T>, IHibernateFinderDAO<T> {
}