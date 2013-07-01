/**
 * 
 */
package sk.seges.acris.security.server.spring.user_management.dao.user.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import sk.seges.acris.security.server.core.user_management.domain.jpa.JpaUserWithAuthorities;
import sk.seges.acris.security.server.spring.user_management.dao.user.api.IGenericUserDao;
import sk.seges.acris.security.shared.user_management.domain.api.GroupAuthoritiesHolder;
import sk.seges.acris.security.shared.user_management.domain.api.UserPermission;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;
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
	public JpaUserWithAuthorities findByUsername(String username) {
		return (JpaUserWithAuthorities) entityManager
				.createQuery("from " + JpaUserWithAuthorities.class.getName() + " where " + UserData.USERNAME + " = :username")
				.setParameter("username", username).getSingleResult();
	}

	@Override
	public JpaUserWithAuthorities findUser(String username, String webId) {
		return (JpaUserWithAuthorities) entityManager
				.createQuery("from " + JpaUserWithAuthorities.class.getName() + " where " + UserData.USERNAME + " = :username and (" +
						UserData.WEB_ID + " = :webId or " + UserData.WEB_ID + " is null)")
				.setParameter("username", username).setParameter("webId", webId).getSingleResult();
	}
	
	@Override
	public JpaUserWithAuthorities persist(JpaUserWithAuthorities entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JpaUserWithAuthorities merge(JpaUserWithAuthorities entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(JpaUserWithAuthorities entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PagedResult<List<JpaUserWithAuthorities>> findAll(Page requestedPage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JpaUserWithAuthorities findEntity(JpaUserWithAuthorities entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JpaUserWithAuthorities persist(JpaUserWithAuthorities user, GroupAuthoritiesHolder<? extends UserPermission> authoritiesHolder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JpaUserWithAuthorities persist(JpaUserWithAuthorities user, GroupAuthoritiesHolder<? extends UserPermission> authoritiesHolder, boolean addMode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JpaUserWithAuthorities collectUserAuthorities(JpaUserWithAuthorities user, GroupAuthoritiesHolder<? extends UserPermission> authoritiesHolder,
			boolean addMode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JpaUserWithAuthorities findUnique(Page requestedPage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JpaUserWithAuthorities getEntityInstance() {
		return new JpaUserWithAuthorities();
	}
}