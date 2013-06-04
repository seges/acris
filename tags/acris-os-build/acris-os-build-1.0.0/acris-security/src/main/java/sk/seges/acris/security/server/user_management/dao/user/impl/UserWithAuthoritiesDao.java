/**
 * 
 */
package sk.seges.acris.security.server.user_management.dao.user.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import sk.seges.acris.security.rpc.user_management.domain.GenericUser;
import sk.seges.acris.security.rpc.user_management.domain.IGroupAuthoritiesHolder;
import sk.seges.acris.security.rpc.user_management.domain.IUserPermission;
import sk.seges.acris.security.rpc.user_management.domain.UserWithAuthorities;
import sk.seges.acris.security.server.user_management.dao.user.IGenericUserDao;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

/**
 * @author ladislav.gazo
 */
public class UserWithAuthoritiesDao implements IGenericUserDao {
	private EntityManager entityManager;

	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public GenericUser collectUserAuthorities(GenericUser user,
			IGroupAuthoritiesHolder<? extends IUserPermission> authoritiesHolder, boolean addMode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserWithAuthorities findByUsername(String username) {
		return (UserWithAuthorities) entityManager.createQuery(
				"from " + UserWithAuthorities.class.getName() + " where " + GenericUser.USER_NAME_ATTRIBUTE
						+ " = :username").setParameter("username", username).getSingleResult();
	}

	@Override
	public GenericUser persist(GenericUser user,
			IGroupAuthoritiesHolder<? extends IUserPermission> authoritiesHolder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericUser persist(GenericUser user,
			IGroupAuthoritiesHolder<? extends IUserPermission> authoritiesHolder, boolean addMode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericUser merge(GenericUser entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericUser persist(GenericUser entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(GenericUser entity) {
	// TODO Auto-generated method stub

	}

	@Override
	public PagedResult<List<GenericUser>> findAll(Page requestedPage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericUser findEntity(GenericUser entity) {
		// TODO Auto-generated method stub
		return null;
	}

}
