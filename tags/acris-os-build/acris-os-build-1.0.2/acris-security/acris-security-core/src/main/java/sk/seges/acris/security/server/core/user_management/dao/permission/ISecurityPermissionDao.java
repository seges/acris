package sk.seges.acris.security.server.core.user_management.dao.permission;

import sk.seges.acris.security.shared.user_management.domain.api.HierarchyPermission;
import sk.seges.sesam.dao.ICrudDAO;

public interface ISecurityPermissionDao<T extends HierarchyPermission> extends ICrudDAO<T> {}
