/**
 * 
 */
package sk.seges.acris.security.server.spring.user_management.dao.user.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import sk.seges.acris.security.server.core.user_management.domain.jpa.JpaGenericUser;
import sk.seges.acris.security.server.spring.user_management.domain.jpa.JpaSpringUserWithAuthorities;
import sk.seges.acris.security.server.user_management.dao.user.IGenericUserDao;
import sk.seges.acris.security.shared.user_management.domain.api.GroupAuthoritiesHolder;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.acris.security.shared.user_management.domain.api.UserDataBeanWrapper;
import sk.seges.acris.security.shared.user_management.domain.api.UserPermission;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

/**
 * @author ladislav.gazo
 */
public class UserWithAuthoritiesDao implements IGenericUserDao<JpaGenericUser> {

	private EntityManager entityManager;

	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public UserData collectUserAuthorities(UserData user, GroupAuthoritiesHolder<? extends UserPermission> authoritiesHolder, boolean addMode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserData findByUsername(String username) {
		return (UserData) entityManager
				.createQuery("from " + JpaSpringUserWithAuthorities.class.getName() + " where " + UserDataBeanWrapper.USERNAME + " = :username")
				.setParameter("username", username).getSingleResult();
	}

	@Override
	public UserData persist(UserData user, GroupAuthoritiesHolder<? extends UserPermission> authoritiesHolder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserData persist(UserData user, GroupAuthoritiesHolder<? extends UserPermission> authoritiesHolder, boolean addMode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JpaGenericUser merge(JpaGenericUser entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JpaGenericUser persist(JpaGenericUser entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(JpaGenericUser entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public PagedResult<List<JpaGenericUser>> findAll(Page requestedPage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JpaGenericUser findEntity(JpaGenericUser entity) {
		// TODO Auto-generated method stub
		return null;
	}

}
