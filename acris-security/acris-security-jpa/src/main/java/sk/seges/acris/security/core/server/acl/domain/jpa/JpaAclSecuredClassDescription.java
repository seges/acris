package sk.seges.acris.security.core.server.acl.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import sk.seges.acris.security.acl.server.model.base.AclSecuredClassDescriptionBase;

/**
 * Each class from the system whose objects we wish to secure must be registered in the ACLSecuredClass and uniquely
 * identified by Spring Security by an id
 */
@Entity
@Table(name = "ACL_SECURED_CLASS_DESCRIPTION", uniqueConstraints = { @UniqueConstraint(columnNames = { "class" }) })
public class JpaAclSecuredClassDescription extends AclSecuredClassDescriptionBase {

	private static final long serialVersionUID = 1392972668729469987L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return super.getId();
	}

	/**
	 * Is the fully qualified Java name of the class, such as sk.seges.acris.server.acl.ACLSecuredClass.
	 */
	@Column(name="class")
	public String getClassName() {
		return super.getClassName();
	}


}