package sk.seges.acris.security.rpc.service;

import sk.seges.acris.security.rpc.domain.SecurityPermission;

public interface ISecurityPermissionServiceExt extends ISecurityPermissionService {
    public void persist(SecurityPermission rolePermission);
    public void remove(SecurityPermission rolePermission);
}
