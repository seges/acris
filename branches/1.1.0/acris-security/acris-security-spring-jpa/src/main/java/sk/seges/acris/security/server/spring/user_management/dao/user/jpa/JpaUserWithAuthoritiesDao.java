/**
 * 
 */
package sk.seges.acris.security.server.spring.user_management.dao.user.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import sk.seges.acris.security.server.core.user_management.dao.user.IGenericUserDao;
import sk.seges.acris.security.shared.core.user_management.domain.jpa.JpaUserWithAuthorities;
import sk.seges.acris.security.shared.user_management.domain.api.GroupAuthoritiesHolder;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.acris.security.shared.user_management.domain.api.UserDataBeanWrapper;
import sk.seges.acris.security.shared.user_management.domain.api.UserPermission;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

/**
 * @author ladislav.gazo
 */
public class JpaUserWithAuthoritiesDao implements IGenericUserDao<JpaUserWithAuthorities> {

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
				.createQuery("from " + JpaUserWithAuthorities.class.getName() + " where " + UserDataBeanWrapper.USERNAME + " = :username")
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
	public UserData persist(UserData entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserData merge(UserData entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(UserData entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PagedResult<List<UserData>> findAll(Page requestedPage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserData findEntity(UserData entity) {
		// TODO Auto-generated method stub
		return null;
	}

}
