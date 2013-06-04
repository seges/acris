package sk.seges.acris.security.server.acl.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import sk.seges.sesam.domain.IDomainObject;

/**
 * Each class from the system whose objects we wish to secure must be registered in the ACLSecuredClass and uniquely
 * identified by Spring Security by an id
 */
@Entity
@Table(name = "ACL_SECURED_CLASS_DESCRIPTION", uniqueConstraints = { @UniqueConstraint(columnNames = { "class" }) })
public class AclSecuredClassDescription implements IDomainObject<Long> {
	
	private static final long serialVersionUID = 5346314999000980981L;

	public static final String CLASS_NAME_FIELD = "className";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Is the fully qualified Java name of the class, such as sk.seges.acris.server.acl.ACLSecuredClass.
	 */
	@Column(name="class")
	private String className;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getClassName() {
		return className;
	}


	public void setClassName(String className) {
		this.className = className;
	}
}