package sk.seges.acris.security.server.core.user_management.service.permission;

import java.io.Serializable;
import java.util.List;

import sk.seges.acris.security.server.core.user_management.dao.permission.ISecurityPermissionDao;
import sk.seges.acris.security.shared.user_management.domain.api.HierarchyPermission;
import sk.seges.acris.security.shared.user_management.domain.api.HierarchyPermissionMetaModel;
import sk.seges.acris.security.shared.user_management.domain.api.UserDataMetaModel;
import sk.seges.acris.security.shared.user_management.service.IHierarchyPermissionServiceExt;
import sk.seges.sesam.dao.Conjunction;
import sk.seges.sesam.dao.Filter;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;
import sk.seges.sesam.dao.SimpleExpression;

public class SecurityPermissionService implements IHierarchyPermissionServiceExt {

	private ISecurityPermissionDao<HierarchyPermission> securityPermissionDao;

	public SecurityPermissionService(ISecurityPermissionDao<HierarchyPermission> securityPermissionDao) {
		this.securityPermissionDao = securityPermissionDao;
	}
	
	public ISecurityPermissionDao<HierarchyPermission> getRolePermissionDao() {
		return securityPermissionDao;
	}

	@Override
	public PagedResult<List<HierarchyPermission>> findAll(String webId, Page page) {
		
		SimpleExpression<Comparable<? extends Serializable>> eq = Filter.eq(HierarchyPermissionMetaModel.WEB_ID);
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
	public void persist(HierarchyPermission rolePermission) {
		securityPermissionDao.persist(rolePermission);
	}

	@Override
	public void remove(HierarchyPermission rolePermission) {
		rolePermission = securityPermissionDao.findEntity(rolePermission);
		if (null != rolePermission) {
			securityPermissionDao.remove(rolePermission);
		}
	}

	@Override
	public List<HierarchyPermission> findSecurityPermissions(Integer parentId, String webId) {
		Page page = new Page(0, 0);
		if (null == parentId) {
			Conjunction filterable = Filter.conjunction(); 
			filterable.add(Filter.isNull(HierarchyPermissionMetaModel.PARENT.THIS));
			SimpleExpression<Comparable<? extends Serializable>> eq = Filter.eq(HierarchyPermissionMetaModel.WEB_ID);
			eq.setValue(webId);
			filterable.add(eq);
			page.setFilterable(filterable);
		} else {
			Conjunction filterable = Filter.conjunction(); 
			//FIXME, interface inheritance
			SimpleExpression<Comparable<? extends Serializable>> eq = Filter.eq(HierarchyPermissionMetaModel.PARENT.THIS + ".id");
			eq.setValue(parentId);
			filterable.add(eq);
			
			eq = Filter.eq(HierarchyPermissionMetaModel.WEB_ID);
			eq.setValue(webId);
			filterable.add(eq);
			page.setFilterable(filterable);
		}
		return securityPermissionDao.findAll(page).getResult();
	}
}