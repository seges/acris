package sk.seges.acris.security.server.dao.permission.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.rpc.domain.SecurityRole;
import sk.seges.acris.security.server.dao.permission.ISecurityRoleDao;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;

@Component
public class SecurityRoleDao extends AbstractHibernateCRUD<SecurityRole> implements ISecurityRoleDao {

    @PersistenceContext(unitName = "hrOddelenieEntityManagerFactory")
    public void setEntityManager(EntityManager em) {
        super.setEntityManager(em);
    }

    protected SecurityRoleDao() {
        super(SecurityRole.class);
    }

//    private static final String FIND_ROLE_PERMISSIONS_HQL = "select r.rolePermissions from SecurityRole r where r.id=:roleId";
//    
//    @Transactional(propagation=Propagation.SUPPORTS)
//    public List<SecurityPermission> findRolePermissions(Integer roleId) {
//        Session hibernateSession = (Session) entityManager.getDelegate();
//        org.hibernate.Query query = hibernateSession.createQuery(FIND_ROLE_PERMISSIONS_HQL);
//        query.setParameter("roleId", roleId);
//        return query.list();
//    }

    private static final String FIND_USER_PERMISSIONS_SQL = "select element from role_selectedpermissions  where role_id=:roleId";
    
    @Transactional(propagation=Propagation.SUPPORTS)
    public List<String> findSelectedPermissions(Integer roleId) {
        Session hibernateSession = (Session) entityManager.getDelegate();
        SQLQuery query = hibernateSession.createSQLQuery(FIND_USER_PERMISSIONS_SQL);
        query.setParameter("roleId", roleId);
        return query.list();
    }
}
