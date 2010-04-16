package sk.seges.acris.security.server.acl.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import sk.seges.sesam.domain.IDomainObject;

@Entity
@Table(name = "ACL_ENTRY", 
		uniqueConstraints = { @UniqueConstraint(columnNames = { "acl_object_identity", "ace_order" }) })
public class AclEntry implements IDomainObject<Long> {
	
	private static final long serialVersionUID = -4884111810306823376L;

    public static final String A_SID = "sid"; 
    public static final String A_SID_ID = "sid.id"; 
    public static final String A_OBJECT_IDENTITY = "objectIdentity"; 
	public static final String A_OBJECT_IDENTITY_ID = "objectIdentity.id"; 
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Secured object descriptor */
	@ManyToOne
	@JoinColumn(name="acl_object_identity", nullable=false)
	private AclSecuredObjectIdentity objectIdentity;

	/** The user or role id which may perform the action */
	@ManyToOne
	@JoinColumn(name="sid", nullable=false)
	private AclSid sid;

	/**
	 * The permissions to be granted or denied are specified by using the field mask as a bitfield. For example, if mask
	 * is set to the 8-bit value 00000101, then actions 0 and 2 are allowed, while action 1 is disallowed. Note that the
	 * meaning of these actions (such as read, write, delete) is application-dependent. The granting field, if set to
	 * true, indicates that the permissions indicated by mask are granted to the corresponding sid, otherwise they are
	 * revoked or blocked
	 */
	@Column(nullable = false)
	private int mask;

	@Column(name="ace_order", nullable = false)
	private int aceOrder;

	@Column(nullable = false)
	private boolean granting;

	@Column(name="audit_success", nullable = false)
	private boolean auditSuccess;

	@Column(name="audit_failure", nullable = false)
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