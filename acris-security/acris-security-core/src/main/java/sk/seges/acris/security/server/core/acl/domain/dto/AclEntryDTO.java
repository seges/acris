package sk.seges.acris.security.server.core.acl.domain.dto;

import sk.seges.acris.security.server.core.acl.domain.api.AclEntry;
import sk.seges.acris.security.server.core.acl.domain.api.AclSecuredObjectIdentity;
import sk.seges.acris.security.server.core.acl.domain.api.AclSid;

public class AclEntryDTO implements AclEntry {

	private static final long serialVersionUID = -4884111810306823376L;

	private Long id;

	private AclSecuredObjectIdentity objectIdentity;

	private AclSid sid;

	/**
	 * The permissions to be granted or denied are specified by using the field mask as a bitfield. For example, if mask
	 * is set to the 8-bit value 00000101, then actions 0 and 2 are allowed, while action 1 is disallowed. Note that the
	 * meaning of these actions (such as read, write, delete) is application-dependent. The granting field, if set to
	 * true, indicates that the permissions indicated by mask are granted to the corresponding sid, otherwise they are
	 * revoked or blocked
	 */
	private int mask;

	private int aceOrder;

	private boolean granting;

	private boolean auditSuccess;

	private boolean auditFailure;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AclSecuredObjectIdentity getObjectIdentity() {
		return objectIdentity;
	}

	public void setObjectIdentity(AclSecuredObjectIdentity objectIdentity) {
		this.objectIdentity = objectIdentity;
	}

	public AclSid getSid() {
		return sid;
	}

	public void setSid(AclSid sid) {
		this.sid = sid;
	}

	public int getMask() {
		return mask;
	}

	public void setMask(int mask) {
		this.mask = mask;
	}

	public int getAceOrder() {
		return aceOrder;
	}

	public void setAceOrder(int aceOrder) {
		this.aceOrder = aceOrder;
	}

	public boolean isGranting() {
		return granting;
	}

	public void setGranting(boolean granting) {
		this.granting = granting;
	}

	public boolean isAuditSuccess() {
		return auditSuccess;
	}

	public void setAuditSuccess(boolean auditSuccess) {
		this.auditSuccess = auditSuccess;
	}

	public boolean isAuditFailure() {
		return auditFailure;
	}

	public void setAuditFailure(boolean auditFailure) {
		this.auditFailure = auditFailure;
	}
}