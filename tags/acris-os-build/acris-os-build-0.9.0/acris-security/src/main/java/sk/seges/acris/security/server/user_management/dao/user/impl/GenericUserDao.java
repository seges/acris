package sk.seges.acris.security.server.user_management.dao.user.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.rpc.user_management.domain.GenericUser;
import sk.seges.acris.security.rpc.user_management.domain.IGroupAuthoritiesHolder;
import sk.seges.acris.security.rpc.user_management.domain.IUserPermission;
import sk.seges.acris.security.rpc.user_management.domain.RolePermission;
import sk.seges.acris.security.server.user_management.dao.user.IGenericUserDao;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;

@Component
@Transactional
public class GenericUserDao extends AbstractHibernateCRUD<GenericUser> implements IGenericUserDao {

	public GenericUserDao() {
		super(GenericUser.class);
	}

	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager entityManager) {
		super.setEntityManager(entityManager);
	}

	@Transactional
	public GenericUser collectUserAuthorities(GenericUser user, IGroupAuthoritiesHolder<? extends IUserPermission> authoritiesHolder, boolean addMode) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		if (addMode && user.getAuthorities() != null) {
			for (GrantedAuthority authority : user.getAuthorities()) {
				authorities.add(authority);
			}
		}
		
		if (!Hibernate.isInitialized(authoritiesHolder.getRolePermissions())) {
			Hibernate.initialize(authoritiesHolder.getRolePermissions());
		}
		
		for (RolePermission rolePermission : authoritiesHolder.getRolePermissions()) {
			authorities.add(new GrantedAuthorityImpl("ROLE_" + rolePermission.getPermission()));
			
			authorities.addAll(extractAuthorities(rolePermission.getUserPermissions()));
		}

		authorities.addAll(extractAuthorities(authoritiesHolder.getUserPermissions()));

		user.setAuthorities(authorities.toArray(new GrantedAuthority[0]));
		
		return user;
	}

	@Override
	@Transactional
	public GenericUser persist(GenericUser user, IGroupAuthoritiesHolder<? extends IUserPermission> authoritiesHolder) {
		return super.persist(collectUserAuthorities(user, authoritiesHolder, false));
	}
	
	@Override
	public GenericUser persist(GenericUser user, IGroupAuthoritiesHolder<? extends IUserPermission> authoritiesHolder, boolean addMode) {
		return super.persist(collectUserAuthorities(user, authoritiesHolder, addMode));
	}

	private List<GrantedAuthority> extractAuthorities(List<?> userPermissions) {
		if (!Hibernate.isInitialized(userPermissions)) {
			Hibernate.initialize(userPermissions);
		}

		List<GrantedAuthority> userAuthorities = new ArrayList<GrantedAuthority>();
		
		for (Object userPermission : userPermissions) {
			if (userPermission instanceof String) {
				userAuthorities.add(new GrantedAuthorityImpl((String)userPermission));
			} else if (userPermission instanceof IUserPermission) {
				userAuthorities.add(new GrantedAuthorityImpl(((IUserPermission)userPermission).name()));
			}
		}
		
		return userAuthorities;
	}

	@Override
	public GenericUser findByUsername(String username) {
		DetachedCriteria criteria = createCriteria();
		criteria.add(Restrictions.eq(GenericUser.USER_NAME_ATTRIBUTE, username));
		return findUniqueResultByCriteria(criteria);
	}
}