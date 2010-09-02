package sk.seges.acris.security.server.spring.user_management.dao.user.impl;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.server.core.user_management.domain.jpa.JpaGenericUser;
import sk.seges.acris.security.server.user_management.dao.user.impl.HibernateGenericUserDao;
import sk.seges.acris.security.shared.user_management.domain.api.GroupAuthoritiesHolder;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.acris.security.shared.user_management.domain.api.UserPermission;

@Component
@Transactional
public class HibernateSpringGenericUserDao extends HibernateGenericUserDao {

	@Override
	@Transactional
	public JpaGenericUser collectUserAuthorities(UserData user, GroupAuthoritiesHolder<? extends UserPermission> authoritiesHolder, boolean addMode) {
		return super.collectUserAuthorities(user, authoritiesHolder, addMode);
	}

	@Override
	@Transactional
	public JpaGenericUser persist(UserData user, GroupAuthoritiesHolder<? extends UserPermission> authoritiesHolder) {
		return super.persist(user, authoritiesHolder);
	}
	
	@Override
	@Transactional
	public JpaGenericUser persist(UserData user, GroupAuthoritiesHolder<? extends UserPermission> authoritiesHolder, boolean addMode) {
		return super.persist(user, authoritiesHolder, addMode);
	}

	@Override
	@Transactional
	public JpaGenericUser findByUsername(String username) {
		return super.findByUsername(username);
	}
}