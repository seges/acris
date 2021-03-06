package sk.seges.acris.security.server.user_management.domain.jpa;

import sk.seges.acris.security.server.user_management.server.model.base.OpenIDUserBase;
import sk.seges.acris.security.server.core.user_management.domain.hibernate.HibernateGenericUser;
import sk.seges.acris.security.shared.user_management.domain.api.OpenIDProvider;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;

import javax.persistence.*;

@Entity
@Table(name = "openid_user")
public class JPAOpenIDUser extends OpenIDUserBase {

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
	
	@Override
	@Column
	public String getLocale() {
		return super.getLocale();
	}
}