package sk.seges.acris.security.core.server.acl.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import sk.seges.acris.security.server.core.acl.domain.api.AclEntryBeanWrapper;
import sk.seges.acris.security.server.core.acl.domain.api.AclSecuredObjectIdentity;
import sk.seges.acris.security.server.core.acl.domain.api.AclSid;
import sk.seges.acris.security.server.core.acl.domain.dto.AclEntryDTO;
import sk.seges.sesam.domain.IMutableDomainObject;

@Entity
@Table(name = "ACL_ENTRY", uniqueConstraints = {@UniqueConstraint(columnNames = {"acl_object_identity", AclEntryBeanWrapper.DB_ACE_ORDER})})
public class JpaAclEntry extends AclEntryDTO implements IMutableDomainObject<Long> {

	private static final long serialVersionUID = -7561144169564944658L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return super.getId();
	}

	@ManyToOne(targetEntity = JpaAclSecuredObjectIdentity.class)
	@JoinColumn(name = "acl_object_identity", nullable = false)
	public AclSecuredObjectIdentity getObjectIdentity() {
		return super.getObjectIdentity();
	}

	@ManyToOne(targetEntity = JpaAclSid.class)
	@JoinColumn(name = "sid", nullable = false)
	public AclSid getSid() {
		return super.getSid();
	}

	/**
	 * The permissions to be granted or denied are specified by using the field mask as a bitfield. For example, if mask
	 * is set to the 8-bit value 00000101, then actions 0 and 2 are allowed, while action 1 is disallowed. Note that the
	 * meaning of these actions (such as read, write, delete) is application-dependent. The granting field, if set to
	 * true, indicates that the permissions indicated by mask are granted to the corresponding sid, otherwise they are
	 * revoked or blocked
	 */
	@Column(nullable = false)
	public int getMask() {
		return super.getMask();
	}

	@Column(name = AclEntryBeanWrapper.DB_ACE_ORDER, nullable = false)
	public int getAceOrder() {
		return super.getAceOrder();
	}

	@Column(nullable = false)
	public boolean isGranting() {
		return super.isGranting();
	}

	@Column(name = AclEntryBeanWrapper.DB_AUDIT_SUCCESS, nullable = false)
	public boolean isAuditSuccess() {
		return super.isAuditSuccess();
	}

	@Column(name = AclEntryBeanWrapper.DB_AUDIT_FAILURE, nullable = false)
	public boolean isAuditFailure() {
		return super.isAuditFailure();
	}
}