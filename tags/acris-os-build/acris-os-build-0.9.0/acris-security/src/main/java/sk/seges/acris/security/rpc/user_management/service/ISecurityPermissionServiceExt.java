package sk.seges.acris.security.rpc.user_management.service;

import sk.seges.acris.security.rpc.user_management.domain.SecurityPermission;

public interface ISecurityPermissionServiceExt extends ISecurityPermissionService {
    public void persist(SecurityPermission rolePermission);
    public void remove(SecurityPermission rolePermission);
}
