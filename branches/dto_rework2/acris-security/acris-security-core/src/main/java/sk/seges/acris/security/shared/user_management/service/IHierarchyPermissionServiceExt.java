package sk.seges.acris.security.shared.user_management.service;

import sk.seges.acris.security.shared.user_management.domain.api.HierarchyPermission;

public interface IHierarchyPermissionServiceExt extends IHierarchyPermissionService {

	public void persist(HierarchyPermission rolePermission);

	public void remove(HierarchyPermission rolePermission);
}
