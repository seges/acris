package sk.seges.acris.security.server.user_management.service;

import sk.seges.acris.security.user_management.server.model.data.HierarchyPermissionData;


public interface IHierarchyPermissionServiceExt extends IHierarchyPermissionServiceLocal {

	void persist(HierarchyPermissionData rolePermission);

	void remove(HierarchyPermissionData rolePermission);
}
