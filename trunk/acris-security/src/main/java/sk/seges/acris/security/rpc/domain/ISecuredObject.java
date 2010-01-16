package sk.seges.acris.security.rpc.domain;

import java.io.Serializable;

import sk.seges.sesam.domain.IDomainObject;

public interface ISecuredObject extends Serializable, IDomainObject<Long> {

	public static final String ACL_OBJECT_READ = "ACL_OBJECT_READ";
	public static final String ACL_OBJECT_WRITE = "ACL_OBJECT_WRITE";
	public static final String ACL_OBJECT_DELETE = "ACL_OBJECT_DELETE";
	public static final String ACL_OBJECT_ADMIN = "ACL_OBJECT_ADMIN";
	
	public Long getId();
}
