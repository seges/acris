package sk.seges.acris.security.server.core.user_management.dao.permission.hibernate;

import sk.seges.acris.security.server.core.user_management.dao.permission.ISecurityPermissionDao;
import sk.seges.acris.security.shared.user_management.domain.api.HierarchyPermission;
import sk.seges.corpis.dao.hibernate.IHibernateFinderDAO;

public interface IHibernateSecurityPermissionDao<T extends HierarchyPermission> extends ISecurityPermissionDao<T>, IHibernateFinderDAO<T> {}
