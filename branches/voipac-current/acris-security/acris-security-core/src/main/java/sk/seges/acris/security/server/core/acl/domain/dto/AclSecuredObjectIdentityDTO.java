package sk.seges.acris.security.server.core.acl.domain.dto;

import java.io.Serializable;

import sk.seges.acris.security.server.core.acl.domain.api.AclSecuredClassDescription;
import sk.seges.acris.security.server.core.acl.domain.api.AclSecuredObjectIdentity;
import sk.seges.acris.security.server.core.acl.domain.api.AclSid;

/**
 * Represents every secured object in the system
 */

public class AclSecuredObjectIdentityDTO implements AclSecuredObjectIdentity {

	private static final long serialVersionUID = 1441130439977944451L;

	private Long id;

	private AclSecuredClassDescription objectIdClass;

	private Long objectIdIdentity;

	/**
	 * Owner of the secured object
	 */
	private AclSid ownerSid;

	/**
	 * If the object was inherited, the fields parentObject and entriesInheriting are used to give the due details
	 */
	private boolean entriesInheriting;

	/**
	 * If the object was inherited, the fields parentObject and entriesInheriting are used to give the due details
	 */
	private AclSecuredObjectIdentity parentObject;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AclSecuredClassDescription getObjectIdClass() {
		return objectIdClass;
	}

	public void setObjectIdClass(AclSecuredClassDescription objectIdClass) {
		this.objectIdClass = objectIdClass;
	}

	public AclSecuredObjectIdentity getParentObject() {
		return parentObject;
	}

	public void setParentObject(AclSecuredObjectIdentity parentObject) {
		this.parentObject = parentObject;
	}

	public Long getObjectIdIdentity() {
		return objectIdIdentity;
	}

	public void setObjectIdIdentity(Long objectIdIdentity) {
		this.objectIdIdentity = objectIdIdentity;
	}

	public AclSid getSid() {
		return ownerSid;
	}

	public void setSid(AclSid sid) {
		this.ownerSid = sid;
	}

	public boolean isEntriesInheriting() {
		return entriesInheriting;
	}

	public void setEntriesInheriting(boolean entriesInheriting) {
		this.entriesInheriting = entriesInheriting;
	}

	public Serializable getIdentifier() {
		return id;
	}

	public Class<?> getJavaType() {
		try {
			return Class.forName(objectIdClass.getClassName());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}