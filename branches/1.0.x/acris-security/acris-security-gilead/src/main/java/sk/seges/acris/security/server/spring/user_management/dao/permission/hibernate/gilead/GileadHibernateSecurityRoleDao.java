package sk.seges.acris.security.server.spring.user_management.dao.permission.hibernate.gilead;

import sk.seges.acris.security.server.spring.user_management.dao.permission.hibernate.HibernateSecurityRoleDao;
import sk.seges.acris.security.shared.core.user_management.domain.hibernate.gilead.GileadHibernateSecurityRole;

public class GileadHibernateSecurityRoleDao extends HibernateSecurityRoleDao {

	protected GileadHibernateSecurityRoleDao() {
		super(GileadHibernateSecurityRole.class);
	}

}