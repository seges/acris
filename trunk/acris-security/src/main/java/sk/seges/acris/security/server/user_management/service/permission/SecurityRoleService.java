package sk.seges.acris.security.server.user_management.service.permission;

import java.util.List;

import net.sf.gilead.gwt.PersistentRemoteService;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.rpc.user_management.domain.SecurityRole;
import sk.seges.acris.security.rpc.user_management.service.ISecurityRoleService;
import sk.seges.acris.security.server.user_management.dao.permission.ISecurityRoleDao;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

@Service
public class SecurityRoleService extends PersistentRemoteService implements ISecurityRoleService {

    private static final long serialVersionUID = 6855778501707311971L;

    @Autowired
    private ISecurityRoleDao securityRoleDao;
    
    public ISecurityRoleDao getRoleDao() {
        return securityRoleDao;
    }

    public void setRoleDao(ISecurityRoleDao roleDao) {
        this.securityRoleDao = roleDao;
    }

    @Override
    @Transactional
    public SecurityRole findRole(String roleName) {
        DetachedCriteria criteria = DetachedCriteria.forClass(SecurityRole.class);
        criteria.add(Restrictions.eq(SecurityRole.A_NAME, roleName));
        return securityRoleDao.findUniqueResultByCriteria(criteria);
    }
    
    @Override
    @Transactional
    public SecurityRole persist(SecurityRole role) {
        return securityRoleDao.persist(role);
    }

    @Override
    @Transactional
    public PagedResult<List<SecurityRole>> findAll(Page page) {
        return securityRoleDao.findAll(page);
    }

    @Override
    @Transactional
    public SecurityRole merge(SecurityRole role) {
        return securityRoleDao.merge(role);
    }

    @Override
    @Transactional
    public void remove(SecurityRole role) {
        role = securityRoleDao.findEntity(role);
        if(null != role) {
            securityRoleDao.remove(role);
        }
    }

    @Override
    @Transactional
    public List<String> findSelectedPermissions(Integer roleId) {
        return securityRoleDao.findSelectedPermissions(roleId);
    }
}
