package sk.seges.acris.security.server.spring.user_management.dao.user.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import sk.seges.acris.security.server.user_management.dao.user.IGenericUserDao;
import sk.seges.acris.security.shared.core.user_management.domain.hibernate.HibernateGenericUser;
import sk.seges.acris.security.shared.user_management.domain.api.GroupAuthoritiesHolder;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.acris.security.shared.user_management.domain.api.UserDataBeanWrapper;
import sk.seges.acris.security.shared.user_management.domain.api.UserPermission;
import sk.seges.acris.security.shared.user_management.domain.api.UserRolePermission;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;

public class HibernateGenericUserDao extends AbstractHibernateCRUD<UserData> implements IGenericUserDao<UserData> {

	public HibernateGenericUserDao() {
		super(HibernateGenericUser.class);
	}

	public <T extends UserData> HibernateGenericUserDao(Class<T> clazz) {
		super(clazz);
	}
	
	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager entityManager) {
		super.setEntityManager(entityManager);
	}

	@Override
	public UserData collectUserAuthorities(UserData user, GroupAuthoritiesHolder<? extends UserPermission> authoritiesHolder, boolean addMode) {
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
		
		return user;
	}

	@Override
	public UserData persist(UserData user, GroupAuthoritiesHolder<? extends UserPermission> authoritiesHolder) {
		return super.persist(collectUserAuthorities(user, authoritiesHolder, false));
	}
	
	@Override
	public UserData persist(UserData user, GroupAuthoritiesHolder<? extends UserPermission> authoritiesHolder, boolean addMode) {
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
	public UserData findByUsername(String username) {
		DetachedCriteria criteria = createCriteria();
		criteria.add(Restrictions.eq(UserDataBeanWrapper.USERNAME, username));
		return findUniqueResultByCriteria(criteria);
	}
}