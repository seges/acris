package sk.seges.acris.security.server.user_management.dao.user.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import sk.seges.acris.security.server.core.user_management.domain.jpa.JpaGenericUser;
import sk.seges.acris.security.server.user_management.dao.user.IGenericUserDao;
import sk.seges.acris.security.shared.user_management.domain.api.GroupAuthoritiesHolder;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.acris.security.shared.user_management.domain.api.UserDataBeanWrapper;
import sk.seges.acris.security.shared.user_management.domain.api.UserPermission;
import sk.seges.acris.security.shared.user_management.domain.api.UserRolePermission;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;

public class HibernateGenericUserDao extends AbstractHibernateCRUD<JpaGenericUser> implements IGenericUserDao<JpaGenericUser> {

	public HibernateGenericUserDao() {
		super(JpaGenericUser.class);
	}

	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager entityManager) {
		super.setEntityManager(entityManager);
	}

	@Override
	public JpaGenericUser collectUserAuthorities(UserData user, GroupAuthoritiesHolder<? extends UserPermission> authoritiesHolder, boolean addMode) {
		List<String> authorities = new ArrayList<String>();
		
		if (addMode && user.getUserAuthorities() != null) {
			for (String authority : user.getUserAuthorities()) {
				authorities.add(authority);
			}
		}
		
		if (!Hibernate.isInitialized(authoritiesHolder.getRolePermissions())) {
			Hibernate.initialize(authoritiesHolder.getRolePermissions());
		}
		
		for (UserRolePermission rolePermission : authoritiesHolder.getRolePermissions()) {
			authorities.add("ROLE_" + rolePermission.getPermission());
			
			authorities.addAll(extractAuthorities(rolePermission.getUserPermissions()));
		}

		authorities.addAll(extractAuthorities(authoritiesHolder.getUserPermissions()));

		user.setUserAuthorities(authorities);
		
		return (JpaGenericUser)user;
	}

	@Override
	public JpaGenericUser persist(UserData user, GroupAuthoritiesHolder<? extends UserPermission> authoritiesHolder) {
		return super.persist(collectUserAuthorities(user, authoritiesHolder, false));
	}
	
	@Override
	public JpaGenericUser persist(UserData user, GroupAuthoritiesHolder<? extends UserPermission> authoritiesHolder, boolean addMode) {
		return super.persist(collectUserAuthorities(user, authoritiesHolder, addMode));
	}

	private List<String> extractAuthorities(List<?> userPermissions) {
		if (!Hibernate.isInitialized(userPermissions)) {
			Hibernate.initialize(userPermissions);
		}

		List<String> userAuthorities = new ArrayList<String>();
		
		for (Object userPermission : userPermissions) {
			if (userPermission instanceof String) {
				userAuthorities.add((String)userPermission);
			} else if (userPermission instanceof UserPermission) {
				userAuthorities.add(((UserPermission)userPermission).name());
			}
		}
		
		return userAuthorities;
	}

	@Override
	public JpaGenericUser findByUsername(String username) {
		DetachedCriteria criteria = createCriteria();
		criteria.add(Restrictions.eq(UserDataBeanWrapper.USERNAME, username));
		return findUniqueResultByCriteria(criteria);
	}
}