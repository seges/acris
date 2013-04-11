package sk.seges.sesam.security.server.model.acl;

import sk.seges.sesam.domain.IDomainObject;
import sk.seges.sesam.security.shared.model.api.PermissionData;

public class AclSecuredEntity<T extends IDomainObject<?>> {

	private T entity;
	
	private PermissionData permissionData;
	
	private AclSecurityData aclData;

	public AclSecuredEntity() {}

	public AclSecuredEntity(T t, PermissionData permissionData, AclSecurityData aclData) {
		this.entity = t;
		this.permissionData = permissionData;
		this.aclData = aclData;
	}
	
	public T getEntity() {
		return entity;
	}
	
	public void setEntity(T entity) {
		this.entity = entity;
	}
	
	public PermissionData getPermissionData() {
		return permissionData;
	}

	public void setPermissionData(PermissionData permissionData) {
		this.permissionData = permissionData;
	}
	
	public AclSecurityData getAclData() {
		return aclData;
	}
	
	public void setAclData(AclSecurityData aclData) {
		this.aclData = aclData;
	}
}