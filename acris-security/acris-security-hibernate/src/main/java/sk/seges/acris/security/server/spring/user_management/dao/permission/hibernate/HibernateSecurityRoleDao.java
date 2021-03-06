package sk.seges.acris.security.server.spring.user_management.dao.permission.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import sk.seges.acris.security.server.core.user_management.dao.permission.hibernate.IHibernateSecurityRoleDao;
import sk.seges.acris.security.server.core.user_management.domain.hibernate.HibernateSecurityRole;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;
import sk.seges.corpis.server.domain.user.server.model.data.RoleData;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.utils.CastUtils;

public class HibernateSecurityRoleDao extends AbstractHibernateCRUD<RoleData> implements IHibernateSecurityRoleDao<RoleData> {

	@Override
	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager em) {
		super.setEntityManager(em);
	}

	protected HibernateSecurityRoleDao() {
		super(HibernateSecurityRole.class);
	}

	protected HibernateSecurityRoleDao(Class<? extends RoleData> clazz) {
		super(clazz);
	}

	private static final String FIND_USER_AUTHORITIES_SQL = "select selectedauthorities_element from role_selectedauthorities where role_id=:roleId";

	@Override
	@SuppressWarnings("unchecked")
	public List<String> findSelectedPermissions(Integer roleId) {
		Session hibernateSession = (Session) entityManager.getDelegate();
		SQLQuery query = hibernateSession.createSQLQuery(FIND_USER_AUTHORITIES_SQL);
		query.setParameter("roleId", roleId);
		return query.list();
	}

	@Override
	public RoleData findByName(String name, String webId) {
		DetachedCriteria criteria = createCriteria();
		criteria.add(Restrictions.eq(RoleData.NAME, name));
		criteria.add(Restrictions.eq(RoleData.WEB_ID, webId));
		return findUniqueResultByCriteria(criteria);
	}

	@Override
	public RoleData findUnique(Page page) {
		List<RoleData> result = CastUtils.cast(findByCriteria(createCriteria(), page), RoleData.class);
		
		if (result == null || result.size() == 0) {
			throw new RuntimeException("Unable to obtain unique result. No results found!");
		}
		
		if (result.size() > 1) {
			throw new RuntimeException("Unable to obtain unique result. More than 1 result found!");
		}
		
		return result.get(0);
	}
}