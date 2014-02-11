/**
 * 
 */
package sk.seges.acris.security.server.core.user_management;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import sk.seges.acris.security.shared.core.user_management.domain.hibernate.HibernateGenericUser;
import sk.seges.acris.security.shared.core.user_management.domain.hibernate.HibernateSecurityRole;
import sk.seges.corpis.server.domain.user.server.model.base.UserRoleBase;
import sk.seges.corpis.server.domain.user.server.model.data.RoleData;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;
import sk.seges.sesam.model.metadata.annotation.MetaModel;

/**
 * @author ladislav.gazo
 */
@Entity
@Table(name = "webid_user_role", uniqueConstraints = { @UniqueConstraint(columnNames = { WebIDUserRole.USER_ID,
		WebIDUserRole.WEB_ID, WebIDUserRole.SECURITY_ROLE_ID }) })
@MetaModel
public class WebIDUserRole extends UserRoleBase {
	private static final long serialVersionUID = 1759219506025466660L;

	protected static final String SECURITY_ROLE_ID = "security_role_id";
	protected static final String USER_ID = "user_id";

	public static final String ID = "id";
	public static final String WEB_ID = "webId";
	public static final String PRIORITY = "priority";
	public static final String ROLE = "role";
	public static final String USER = "user";

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return super.getId();
	}

	public void setId(Long id) {
		super.setId(id);
	}

	public String getWebId() {
		return super.getWebId();
	}

	public void setWebId(String webId) {
		super.setWebId(webId);
	}

	public Integer getPriority() {
		return super.getPriority();
	}

	public void setPriority(Integer priority) {
		super.setPriority(priority);
	}

	@ManyToOne(targetEntity = HibernateSecurityRole.class)
	@JoinColumn(name = SECURITY_ROLE_ID)
	public RoleData getRole() {
		return super.getRole();
	}

	public void setRole(RoleData role) {
		super.setRole(role);
	}

	@ManyToOne(targetEntity = HibernateGenericUser.class)
	@JoinColumn(name = USER_ID)
	public UserData getUser() {
		return super.getUser();
	}

	public void setUser(UserData user) {
		super.setUser(user);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getRole() == null) ? 0 : getRole().hashCode());
		result = prime * result + ((getWebId() == null) ? 0 : getWebId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WebIDUserRole other = (WebIDUserRole) obj;
		if (getRole() == null) {
			if (other.getRole() != null)
				return false;
		} else if (!getRole().equals(other.getRole()))
			return false;
		if (getWebId() == null) {
			if (other.getWebId() != null)
				return false;
		} else if (!getWebId().equals(other.getWebId()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WebIDUserRole [id=" + getId() + ", priority=" + getPriority() + ", role=" + getRole() + ", webId=" + getWebId()
				+ "]";
	}
}
