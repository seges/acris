package sk.seges.acris.security.server.acl.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.security.acls.objectidentity.ObjectIdentity;
import org.springframework.util.ReflectionUtils;

import sk.seges.sesam.domain.IDomainObject;

/**
 * Represents every secured object in the system
 */
@Entity
@Table(name = "ACL_SECURED_OBJECT_IDENTITY", uniqueConstraints = { @UniqueConstraint(columnNames = { "object_id_class", "object_id_identity" }) })
public class AclSecuredObjectIdentity implements ObjectIdentity, IDomainObject<Long> {

	public static final String DB_OBJECT_IDENTITY_ID = "object_id_identity";

	private static final long serialVersionUID = 1441130439977944451L;

	public static final String A_CLASS_ID = "objectIdClass.id";
	public static final String A_OBJECT_IDENTITY_ID = "objectIdIdentity";
	public static final String A_OBJECT_CLASS = "objectIdClass";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name="object_id_class",nullable=false)
	private AclSecuredClassDescription objectIdClass;

	@Column(name=DB_OBJECT_IDENTITY_ID, nullable = false)
	private Long objectIdIdentity;

	/**
	 * Owner of the secured object
	 */
	@ManyToOne
	@JoinColumn(name="owner_sid", nullable=false)
	private AclSid ownerSid;

	/**
	 * If the object was inherited, the fields parentObject and entriesInheriting are used to give the due details
	 */
	@Column(name="entries_inheriting", nullable = false)
	private boolean entriesInheriting;

	/**
	 * If the object was inherited, the fields parentObject and entriesInheriting are used to give the due details
	 */
	@ManyToOne
	@JoinColumn(name="parent_object")
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
            ReflectionUtils.handleReflectionException(ex);
        }
        
        return null;
	}
}