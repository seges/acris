package sk.seges.acris.security.server.user_management.domain.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import sk.seges.acris.security.shared.core.user_management.domain.hibernate.HibernateGenericUser;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.acris.security.shared.user_management.domain.dto.OpenIDUserDTO;

@Entity
public class JPAOpenIDUser extends OpenIDUserDTO {

	private static final long serialVersionUID = -8717355972259864251L;

	@Override
	@OneToMany(targetEntity = HibernateGenericUser.class)
	public UserData<?> getUser() {
		return super.getUser();
	}

	@Id
	@Override
	public String getId() {
		return super.getId();
	}
}
