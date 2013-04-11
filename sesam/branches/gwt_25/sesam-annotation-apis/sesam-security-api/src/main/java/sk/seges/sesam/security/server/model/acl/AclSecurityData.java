package sk.seges.sesam.security.server.model.acl;

public class AclSecurityData {

	public static final String ACL_OBJECT_READ = "ACL_OBJECT_READ";
	public static final String ACL_OBJECT_WRITE = "ACL_OBJECT_WRITE";
	public static final String ACL_OBJECT_DELETE = "ACL_OBJECT_DELETE";
	public static final String ACL_OBJECT_ADMIN = "ACL_OBJECT_ADMIN";
	
	private final Long aclId;
	private final String className;
	private final AclSecurityData parentAcl;

	public AclSecurityData(Long aclId, String className) {
		this.aclId = aclId;
		this.className = className;
		this.parentAcl = null;
	}

	public AclSecurityData(Long aclId, String className, AclSecurityData parentAcl) {
		this.aclId = aclId;
		this.className = className;
		this.parentAcl = parentAcl;
	}
	
	public Long getAclId() {
		return aclId;
	}
	
	public String getClassName() {
		return className;
	}
	
	public Class<?> getAclClass() {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	public AclSecurityData getParentAcl() {
		return parentAcl;
	}
}