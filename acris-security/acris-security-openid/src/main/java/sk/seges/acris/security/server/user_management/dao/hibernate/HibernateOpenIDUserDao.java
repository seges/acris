package sk.seges.acris.security.server.user_management.dao.hibernate;

import sk.seges.acris.security.server.user_management.dao.api.IOpenIDUserDao;
import sk.seges.acris.security.server.user_management.domain.api.HasOpenIDIdentifier;
import sk.seges.acris.security.server.user_management.domain.jpa.JPAOpenIDUser;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;
import sk.seges.sesam.dao.IEntityInstancer;

public class HibernateOpenIDUserDao extends AbstractHibernateCRUD<HasOpenIDIdentifier> implements
		IOpenIDUserDao<HasOpenIDIdentifier>, IEntityInstancer<JPAOpenIDUser> {

	public HibernateOpenIDUserDao() {
		super(JPAOpenIDUser.class);
	}

	@Override
	public JPAOpenIDUser getEntityInstance() {
		return new JPAOpenIDUser();
	}
}
