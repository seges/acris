package sk.seges.acris.security.server.user_management.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import sk.seges.acris.security.shared.core.user_management.domain.hibernate.HibernateGenericUser;
import sk.seges.acris.security.shared.user_management.domain.api.OpenIDProvider;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.acris.security.shared.user_management.domain.dto.OpenIDUserDTO;

@Entity
@Table(name = "openid_user")
public class JPAOpenIDUser extends OpenIDUserDTO {

	private static final long serialVersionUID = -8717355972259864251L;

	@Override
	@ManyToOne(targetEntity = HibernateGenericUser.class)
	public UserData getUser() {
		return super.getUser();
	}

	@Override
	@Id
	public String getId() {
		return super.getId();
	}

	@Override
	@Column
	public String getEmail() {
		return super.getEmail();
	}

	@Override
	@Column
	public OpenIDProvider getProvider() {
		return super.getProvider();
	}
}
