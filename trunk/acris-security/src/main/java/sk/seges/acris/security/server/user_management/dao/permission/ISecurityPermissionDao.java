package sk.seges.acris.security.server.user_management.dao.permission;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import sk.seges.acris.security.rpc.user_management.domain.SecurityPermission;
import sk.seges.sesam.dao.ICrudDAO;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

public interface ISecurityPermissionDao extends ICrudDAO<SecurityPermission>  {
    public List<SecurityPermission> findByCriteria(DetachedCriteria criteria);
    public PagedResult<List<SecurityPermission>> findPagedResultByCriteria(DetachedCriteria criteria, Page page);
}
