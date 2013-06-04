package sk.seges.acris.security.core.server.acl.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import sk.seges.acris.security.acl.server.model.base.AclSidBase;

/**
 * The table ACL_SID essentially lists all the users in our systems
 */
@Entity
@Table(name = "ACL_SID", uniqueConstraints = {@UniqueConstraint(columnNames = {"sid", "principal"})})
public class JpaAclSid extends AclSidBase {

	private static final long serialVersionUID = 3753027336787453941L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return super.getId();
	}

	/**
	 * The distinction between two possibilities (user vs. role) is made by the value store in the principal column: -
	 * true indicates that the sid is a user - false means that the sid is a granted authority
	 */
	@Column
	public boolean isPrincipal() {
		return super.isPrincipal();
	}

	/**
	 * In Spring Security, a "security id" (SID) is assigned to each user or role. This SID can be then used in an
	 * access control list (ACL) to specify which actions can the user with that SID perform on the desired objects. In
	 * fact, the SID may correspond to an user, a device or a system which can perform an action in the application, or
	 * it may correspond to a granted authority such as a role.
	 */
	@Column
	public String getSid() {
		return super.getSid();
	}
}