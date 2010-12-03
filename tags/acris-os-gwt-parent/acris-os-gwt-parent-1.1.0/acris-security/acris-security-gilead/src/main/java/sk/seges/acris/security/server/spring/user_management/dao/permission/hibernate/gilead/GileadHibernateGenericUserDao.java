package sk.seges.acris.security.server.spring.user_management.dao.permission.hibernate.gilead;

import sk.seges.acris.security.server.spring.user_management.dao.user.hibernate.HibernateGenericUserDao;
import sk.seges.acris.security.shared.core.user_management.domain.hibernate.gilead.GileadHibernateGenericUser;


public class GileadHibernateGenericUserDao extends HibernateGenericUserDao {

	public GileadHibernateGenericUserDao() {
		super(GileadHibernateGenericUser.class);
	}
}
