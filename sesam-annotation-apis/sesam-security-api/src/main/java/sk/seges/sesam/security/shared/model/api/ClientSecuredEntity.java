package sk.seges.sesam.security.shared.model.api;

import java.io.Serializable;

import sk.seges.sesam.shared.domain.api.HasId;

public class ClientSecuredEntity<T extends HasId<?>> implements Serializable {

	private static final long serialVersionUID = -2416055428704091024L;

	private T entity;
	
	private PermissionData securityData;

	public ClientSecuredEntity() {};
	
	public ClientSecuredEntity(T t, PermissionData securityData) {
		this.entity = t;
		this.securityData = securityData;
	}
	
	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}
	
	public PermissionData getPermissionData() {
		return securityData;
	}

	public void setPermissionData(PermissionData securityData) {
		this.securityData = securityData;
	}
}