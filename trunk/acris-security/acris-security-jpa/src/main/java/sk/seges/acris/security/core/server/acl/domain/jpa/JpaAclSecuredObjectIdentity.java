package sk.seges.acris.security.core.server.acl.domain.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import sk.seges.acris.security.acl.server.model.base.AclSecuredObjectIdentityBase;
import sk.seges.acris.security.acl.server.model.data.AclSecuredClassDescriptionData;
import sk.seges.acris.security.acl.server.model.data.AclSecuredObjectIdentityData;
import sk.seges.acris.security.acl.server.model.data.AclSidData;

/**
 * Represents every secured object in the system
 */

@Entity
@Table(name = "ACL_SECURED_OBJECT_IDENTITY", uniqueConstraints = {@UniqueConstraint(columnNames = {"object_id_class", "object_id_identity"})})
public class JpaAclSecuredObjectIdentity extends AclSecuredObjectIdentityBase {
 
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
	public AclSecuredClassDescriptionData getObjectIdClass() {
		return super.getObjectIdClass();
	}

	@Override
	@Column(name = "object_id_identity", nullable = false)
	public Long getObjectIdIdentity() {
		return super.getObjectIdIdentity();
	}

	/**
	 * Owner of the secured object
	 */
	@Override
	@ManyToOne(targetEntity = JpaAclSid.class)
	@JoinColumn(name = "owner_sid", nullable = false)
	public AclSidData getSid() {
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
	public AclSecuredObjectIdentityData getParentObject() {
		return super.getParentObject();
	}

	@Transient
	@Override
	public Serializable getIdentifier() {
		return getId();
	}

	@Transient
	@Override
	public Class<?> getJavaType() {
		try {
			return Class.forName(getObjectIdClass().getClassName());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}