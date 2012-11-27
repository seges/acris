package sk.seges.acris.security.server.core.user_management.dao.permission.api;

import sk.seges.acris.security.user_management.server.model.data.HierarchyPermissionData;
import sk.seges.sesam.dao.ICrudDAO;

public interface ISecurityPermissionDao<T extends HierarchyPermissionData> extends ICrudDAO<T> {}
