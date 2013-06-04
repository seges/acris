package sk.seges.acris.security.server.core.user_management.service.permission;

import java.io.Serializable;
import java.util.List;

import sk.seges.acris.security.server.core.user_management.dao.permission.api.ISecurityPermissionDao;
import sk.seges.acris.security.shared.user_management.service.IHierarchyPermissionServiceExt;
import sk.seges.acris.security.user_management.server.model.data.HierarchyPermissionData;
import sk.seges.sesam.dao.Conjunction;
import sk.seges.sesam.dao.Filter;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;
import sk.seges.sesam.dao.SimpleExpression;
import sk.seges.sesam.pap.service.annotation.LocalService;

@LocalService
public class SecurityPermissionService implements IHierarchyPermissionServiceExt {

	private ISecurityPermissionDao<HierarchyPermissionData> securityPermissionDao;

	public SecurityPermissionService(ISecurityPermissionDao<HierarchyPermissionData> securityPermissionDao) {
		this.securityPermissionDao = securityPermissionDao;
	}
	
	public ISecurityPermissionDao<HierarchyPermissionData> getRolePermissionDao() {
		return securityPermissionDao;
	}

	@Override
	public PagedResult<List<HierarchyPermissionData>> findAll(String webId, Page page) {
		
		SimpleExpression<Comparable<? extends Serializable>> eq = Filter.eq(HierarchyPermissionData.WEB_ID);
		eq.setValue(webId);

		if (page.getFilterable() != null) {
			Conjunction filterable = Filter.conjunction(); 
			filterable.add(page.getFilterable());
			filterable.add(eq);
			page.setFilterable(filterable);
		} else {
			page.setFilterable(eq);
		}

		return securityPermissionDao.findAll(page);
	}

	@Override
	public void persist(HierarchyPermissionData rolePermission) {
		securityPermissionDao.persist(rolePermission);
	}

	@Override
	public void remove(HierarchyPermissionData rolePermission) {
		rolePermission = securityPermissionDao.findEntity(rolePermission);
		if (null != rolePermission) {
			securityPermissionDao.remove(rolePermission);
		}
	}

	@Override
	public List<HierarchyPermissionData> findSecurityPermissions(Integer parentId, String webId) {
		Page page = new Page(0, 0);
		if (null == parentId) {
			Conjunction filterable = Filter.conjunction(); 
			filterable.add(Filter.isNull(HierarchyPermissionData.PARENT));
			SimpleExpression<Comparable<? extends Serializable>> eq = Filter.eq(HierarchyPermissionData.WEB_ID);
			eq.setValue(webId);
			filterable.add(eq);
			page.setFilterable(filterable);
		} else {
			Conjunction filterable = Filter.conjunction(); 
			//FIXME, interface inheritance
			SimpleExpression<Comparable<? extends Serializable>> eq = Filter.eq(HierarchyPermissionData.PARENT + ".id");
			eq.setValue(parentId);
			filterable.add(eq);
			
			eq = Filter.eq(HierarchyPermissionData.WEB_ID);
			eq.setValue(webId);
			filterable.add(eq);
			page.setFilterable(filterable);
		}
		return securityPermissionDao.findAll(page).getResult();
	}
}