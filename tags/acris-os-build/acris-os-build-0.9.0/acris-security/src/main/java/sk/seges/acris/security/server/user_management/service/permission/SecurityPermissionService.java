package sk.seges.acris.security.server.user_management.service.permission;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.rpc.user_management.domain.SecurityPermission;
import sk.seges.acris.security.rpc.user_management.service.ISecurityPermissionServiceExt;
import sk.seges.acris.security.server.user_management.dao.permission.ISecurityPermissionDao;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

@Service
public class SecurityPermissionService implements ISecurityPermissionServiceExt {

    @Autowired
    private ISecurityPermissionDao securityPermissionDao;
    
    public ISecurityPermissionDao getRolePermissionDao() {
        return securityPermissionDao;
    }

    public void setRolePermissionDao(ISecurityPermissionDao rolePermissionDao) {
        this.securityPermissionDao = rolePermissionDao;
    }

    @Override
    @Transactional
    public PagedResult<List<SecurityPermission>> findAll(String webId, Page page) {
        DetachedCriteria criteria = DetachedCriteria.forClass(SecurityPermission.class);
        criteria.add(Restrictions.eq(SecurityPermission.A_WEBID, webId));
        return securityPermissionDao.findPagedResultByCriteria(criteria, page);
    }

    @Override
    @Transactional
    public void persist(SecurityPermission rolePermission) {
        securityPermissionDao.persist(rolePermission);
    }

    @Override
    @Transactional
    public void remove(SecurityPermission rolePermission) {
        rolePermission = securityPermissionDao.findEntity(rolePermission);
        if(null != rolePermission) {
            securityPermissionDao.remove(rolePermission);
        }
    }

    @Override
    @Transactional
    public List<SecurityPermission> findSecurityPermissions(Integer parentId, String webId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(SecurityPermission.class);
        if(null == parentId) {
            criteria.add(Restrictions.and(Restrictions.isNull(SecurityPermission.A_PARENT), Restrictions.eq(SecurityPermission.A_WEBID, webId)));
        } else {
            criteria.add(Restrictions.and(Restrictions.eq(SecurityPermission.A_PARENT + "." + SecurityPermission.A_ID, parentId), Restrictions.eq(SecurityPermission.A_WEBID, webId)));
        }
        return securityPermissionDao.findByCriteria(criteria);
    }
}
