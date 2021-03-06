package sk.seges.acris.security.server.spring.user_management.dao.user.hibernate;

import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;
import sk.seges.acris.security.server.spring.user_management.dao.user.api.IGenericUserDao;
import sk.seges.acris.security.server.core.user_management.domain.hibernate.HibernateGenericUser;
import sk.seges.acris.security.shared.user_management.domain.api.GroupAuthoritiesHolder;
import sk.seges.acris.security.shared.user_management.domain.api.UserPermission;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
	@Transactional
	public UserData collectUserAuthorities(UserData user, GroupAuthoritiesHolder<? extends UserPermission> authoritiesHolder, boolean addMode) {
		List<String> authorities = new ArrayList<String>();
		
		if (addMode && user.getUserAuthorities() != null) {
			for (String authority : user.getUserAuthorities()) {
				authorities.add(authority);
			}
		}
		
		authorities.addAll(extractAuthorities(authoritiesHolder.getUserPermissions()));

		user.setUserAuthorities(authorities);
		
		return user;
	}

	@Override
	@Transactional
	public UserData persist(UserData user, GroupAuthoritiesHolder<? extends UserPermission> authoritiesHolder) {
		return super.persist(collectUserAuthorities(user, authoritiesHolder, false));
	}
	
	@Override
	@Transactional
	public UserData persist(UserData user, GroupAuthoritiesHolder<? extends UserPermission> authoritiesHolder, boolean addMode) {
		return super.persist(collectUserAuthorities(user, authoritiesHolder, addMode));
	}

	protected List<String> extractAuthorities(Collection<?> userPermissions) {
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

	@Transactional
	@Override
	public UserData findUser(String username, String webId) {
		DetachedCriteria criteria = createCriteria();
		criteria.add(Restrictions.eq(UserData.USERNAME, username));
		criteria.add(Restrictions.disjunction().add(Restrictions.eq(UserData.WEB_ID, webId)).
				add(Restrictions.isNull(UserData.WEB_ID)));
		return findUniqueResultByCriteria(criteria);
	}
	
	@Transactional
	@Override
	public UserData findByUsername(String username) {
		DetachedCriteria criteria = createCriteria();
		criteria.add(Restrictions.eq(UserData.USERNAME, username));
		return findUniqueResultByCriteria(criteria);
	}

	@Override
	public UserData getEntityInstance() {
		return new HibernateGenericUser();
	}
}