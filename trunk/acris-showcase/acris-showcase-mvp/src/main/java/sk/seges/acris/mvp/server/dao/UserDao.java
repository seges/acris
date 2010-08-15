package sk.seges.acris.mvp.server.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import sk.seges.acris.mvp.server.dao.core.AbstractJPACRUD;
import sk.seges.acris.security.rpc.user_management.domain.GenericUser;

@Repository
public class UserDao extends AbstractJPACRUD<GenericUser> implements IUserDao {

	@PersistenceContext(unitName = "entityManagerFactory")
	public void setEntityManager(EntityManager entityManager) {
		super.setEntityManager(entityManager);
	}

	protected UserDao() {
		super(GenericUser.class);
	}
}