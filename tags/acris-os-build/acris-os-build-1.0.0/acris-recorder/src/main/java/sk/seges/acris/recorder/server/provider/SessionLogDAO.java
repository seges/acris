package sk.seges.acris.recorder.server.provider;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.recorder.rpc.domain.SessionLog;
import sk.seges.acris.security.rpc.user_management.domain.GenericUser;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;
import sk.seges.sesam.dao.Page;

@Component
@Transactional
public class SessionLogDAO extends AbstractHibernateCRUD<SessionLog> implements ISessionLogDAO {

	public SessionLogDAO() {
		super(SessionLog.class);
	}
	
	@Override
	public SessionLog add(SessionLog log) {
		return super.persist(log);
	}

	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager entityManager) {
		super.setEntityManager(entityManager);
	}

	@Override
	public List<SessionLog> load(String sessionId) {
		DetachedCriteria detachedCriteria = createCriteria();
		detachedCriteria.add(Restrictions.eq(SessionLog.SESSION_ID_ATTRIBUTE, sessionId));
		return findByCriteria(detachedCriteria, Page.ALL_RESULTS_PAGE);
	}

	private static final String LOAD_BY_USERNAME = "select distinct sessionLog." + 
		SessionLog.USER_ATTRIBUTE + "." + GenericUser.USER_NAME_ATTRIBUTE + " from " + SessionLog.class.getName() + " sessionLog";

	@Override
	public List<GenericUser> loadUsers() {
		Query query = entityManager.createQuery(LOAD_BY_USERNAME); 
		return query.getResultList();
	}
	
//	@Override
//	public List<SessionLog> loadByUsername(String username) {
//		DetachedCriteria detachedCriteria = createCriteria();
//		detachedCriteria.add(Restrictions.eq(SessionLog.USER_ATTRIBUTE + "." + User.USER_NAME_ATTRIBUTE, username));
//		return findByCriteria(detachedCriteria, Page.ALL_RESULTS_PAGE);
//	}

	@Override
	public List<SessionLog> load(List<String> sessionIds) {
		DetachedCriteria detachedCriteria = createCriteria();
		for (String sessionId : sessionIds) {
			detachedCriteria.add(Restrictions.eq(SessionLog.SESSION_ID_ATTRIBUTE, sessionId));
		}
		return findByCriteria(detachedCriteria, Page.ALL_RESULTS_PAGE);
	}

	@Override
	public List<SessionLog> load() {
		return super.findAll(Page.ALL_RESULTS_PAGE).getResult();
	}
}