package sk.seges.acris.security.server.core.user_management.dao.permission.hibernate;

import sk.seges.acris.security.server.core.user_management.dao.permission.api.ISecurityPermissionDao;
import sk.seges.acris.security.user_management.server.model.data.HierarchyPermissionData;
import sk.seges.corpis.dao.hibernate.IHibernateFinderDAO;

public interface IHibernateSecurityPermissionDao<T extends HierarchyPermissionData> extends ISecurityPermissionDao<T>, IHibernateFinderDAO<T> {}
