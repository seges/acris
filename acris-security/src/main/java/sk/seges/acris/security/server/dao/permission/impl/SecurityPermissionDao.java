package sk.seges.acris.security.server.dao.permission.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Component;

import sk.seges.acris.security.rpc.domain.SecurityPermission;
import sk.seges.acris.security.server.dao.permission.ISecurityPermissionDao;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;
import sk.seges.sesam.dao.Page;

@Component
public class SecurityPermissionDao extends AbstractHibernateCRUD<SecurityPermission> implements ISecurityPermissionDao {

    @PersistenceContext(unitName = "hrOddelenieEntityManagerFactory")
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
