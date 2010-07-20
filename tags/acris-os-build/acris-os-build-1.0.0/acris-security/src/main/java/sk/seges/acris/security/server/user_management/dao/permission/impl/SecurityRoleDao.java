package sk.seges.acris.security.server.user_management.dao.permission.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.rpc.user_management.domain.SecurityRole;
import sk.seges.acris.security.server.user_management.dao.permission.ISecurityRoleDao;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;

@Component
public class SecurityRoleDao extends AbstractHibernateCRUD<SecurityRole> implements ISecurityRoleDao {

	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager em) {
		super.setEntityManager(em);
	}

	protected SecurityRoleDao() {
		super(SecurityRole.class);
	}

	// private static final String FIND_ROLE_PERMISSIONS_HQL =
	// "select r.rolePermissions from SecurityRole r where r.id=:roleId";
	//
	// @Transactional(propagation=Propagation.SUPPORTS)
	// public List<SecurityPermission> findRolePermissions(Integer roleId) {
	// Session hibernateSession = (Session) entityManager.getDelegate();
	// org.hibernate.Query query =
	// hibernateSession.createQuery(FIND_ROLE_PERMISSIONS_HQL);
	// query.setParameter("roleId", roleId);
	// return query.list();
	// }

	private static final String FIND_USER_AUTHORITIES_SQL = "select selectedauthorities_element from role_selectedauthorities where role_id=:roleId";

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<String> findSelectedPermissions(Integer roleId) {
		Session hibernateSession = (Session) entityManager.getDelegate();
		SQLQuery query = hibernateSession.createSQLQuery(FIND_USER_AUTHORITIES_SQL);
		query.setParameter("roleId", roleId);
		return query.list();
	}

	@Transactional
	public SecurityRole findByName(String name) {
		DetachedCriteria criteria = createCriteria().add(Restrictions.eq(SecurityRole.A_NAME, name));
		return findUniqueResultByCriteria(criteria);
	}
}