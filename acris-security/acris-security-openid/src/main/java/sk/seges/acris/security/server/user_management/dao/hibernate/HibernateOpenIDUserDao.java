package sk.seges.acris.security.server.user_management.dao.hibernate;

import sk.seges.acris.security.server.user_management.dao.api.IOpenIDUserDao;
import sk.seges.acris.security.server.user_management.domain.jpa.JPAOpenIDUser;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;
import sk.seges.sesam.dao.IEntityInstancer;

public class HibernateOpenIDUserDao extends AbstractHibernateCRUD<JPAOpenIDUser> implements
		IOpenIDUserDao<JPAOpenIDUser>, IEntityInstancer<JPAOpenIDUser> {

	protected HibernateOpenIDUserDao() {
		super(JPAOpenIDUser.class);
	}

	@Override
	public JPAOpenIDUser getEntityInstance() {
		return new JPAOpenIDUser();
	}
}
