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

import sk.seges.acris.security.server.core.acl.domain.api.AclSecuredClassDescription;
import sk.seges.acris.security.server.core.acl.domain.api.AclSecuredObjectIdentity;
import sk.seges.acris.security.server.core.acl.domain.api.AclSecuredObjectIdentityMetaModel;
import sk.seges.acris.security.server.core.acl.domain.api.AclSid;
import sk.seges.acris.security.server.core.acl.domain.dto.AclSecuredObjectIdentityDTO;
import sk.seges.sesam.domain.IMutableDomainObject;

/**
 * Represents every secured object in the system
 */

@Entity
@Table(name = "ACL_SECURED_OBJECT_IDENTITY", uniqueConstraints = {@UniqueConstraint(columnNames = {AclSecuredObjectIdentityMetaModel.DB_OBJECT_ID_CLASS.THIS,
		AclSecuredObjectIdentityMetaModel.DB_OBJECT_ID_IDENTITY})})
public class JpaAclSecuredObjectIdentity extends AclSecuredObjectIdentityDTO implements IMutableDomainObject<Long> {

	//	public static final String A_CLASS_ID = "objectIdClass.id";
	//	public static final String A_OBJECT_IDENTITY_ID = "objectIdIdentity";
	//	public static final String A_OBJECT_CLASS = "objectIdClass";

	private static final long serialVersionUID = -8088693898358113341L;

	@Id
	@Override
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return super.getId();
	}

	@Override
	@ManyToOne(targetEntity = JpaAclSecuredClassDescription.class)
	@JoinColumn(name = "object_id_class", nullable = false)
	public AclSecuredClassDescription getObjectIdClass() {
		return super.getObjectIdClass();
	}

	@Override
	@Column(name = AclSecuredObjectIdentityMetaModel.DB_OBJECT_ID_IDENTITY, nullable = false)
	public Long getObjectIdIdentity() {
		return super.getObjectIdIdentity();
	}

	/**
	 * Owner of the secured object
	 */
	@Override
	@ManyToOne(targetEntity = JpaAclSid.class)
	@JoinColumn(name = "owner_sid", nullable = false)
	public AclSid getSid() {
		return super.getSid();
	}

	/**
	 * If the object was inherited, the fields parentObject and entriesInheriting are used to give the due details
	 */
	@Override
	@Column(name = "entries_inheriting", nullable = false)
	public boolean isEntriesInheriting() {
		return super.isEntriesInheriting();
	}

	/**
	 * If the object was inherited, the fields parentObject and entriesInheriting are used to give the due details
	 */
	@Override
	@ManyToOne(targetEntity = JpaAclSecuredObjectIdentity.class)
	@JoinColumn(name = "parent_object")
	public AclSecuredObjectIdentity getParentObject() {
		return super.getParentObject();
	}
}