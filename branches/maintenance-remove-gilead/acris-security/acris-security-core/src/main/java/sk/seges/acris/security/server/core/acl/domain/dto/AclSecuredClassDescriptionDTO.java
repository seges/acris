package sk.seges.acris.security.server.core.acl.domain.dto;

import sk.seges.acris.security.server.core.acl.domain.api.AclSecuredClassDescription;

/**
 * Each class from the system whose objects we wish to secure must be registered in the ACLSecuredClass and uniquely
 * identified by Spring Security by an id
 */
public class AclSecuredClassDescriptionDTO implements AclSecuredClassDescription {

	private static final long serialVersionUID = 5346314999000980981L;

	private Long id;

	/**
	 * Is the fully qualified Java name of the class, such as sk.seges.acris.server.acl.ACLSecuredClass.
	 */
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