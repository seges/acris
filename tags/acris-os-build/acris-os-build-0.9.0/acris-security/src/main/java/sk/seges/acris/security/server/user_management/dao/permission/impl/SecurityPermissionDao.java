package sk.seges.acris.security.server.user_management.dao.permission.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Component;

import sk.seges.acris.security.rpc.user_management.domain.SecurityPermission;
import sk.seges.acris.security.server.user_management.dao.permission.ISecurityPermissionDao;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;
import sk.seges.sesam.dao.Page;

@Component
public class SecurityPermissionDao extends AbstractHibernateCRUD<SecurityPermission> implements ISecurityPermissionDao {

	@PersistenceContext(unitName = "acrisEntityManagerFactory")
    public void setEntityManager(EntityManager em) {
        super.setEntityManager(em);
    }

    public SecurityPermissionDao() {
        super(SecurityPermission.class);
    }

    @Override
    public List<SecurityPermission> findByCriteria(DetachedCriteria criteria) {
        return super.findByCriteria(criteria, Page.ALL_RESULTS_PAGE);
    }
}
