package sk.seges.acris.security.server.spring.user_management.service.permission;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.server.core.user_management.dao.permission.ISecurityPermissionDao;
import sk.seges.acris.security.server.core.user_management.service.permission.SecurityPermissionService;
import sk.seges.acris.security.shared.user_management.domain.api.HierarchyPermission;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

@Service
public class SpringSecurityPermissionService extends SecurityPermissionService {

	@Autowired
	public SpringSecurityPermissionService(@Qualifier(value = "securityPermissionDao") ISecurityPermissionDao<HierarchyPermission> securityPermissionDao) {
		super(securityPermissionDao);
	}

	@Override
	@Transactional
	public PagedResult<List<HierarchyPermission>> findAll(String webId, Page page) {
		return super.findAll(webId, page);
	}

	@Override
	@Transactional
	public void persist(HierarchyPermission rolePermission) {
		super.persist(rolePermission);
	}

	@Override
	@Transactional
	public void remove(HierarchyPermission rolePermission) {
		super.remove(rolePermission);
	}

	@Override
	@Transactional
	public List<HierarchyPermission> findSecurityPermissions(Integer parentId, String webId) {
		return super.findSecurityPermissions(parentId, webId);
	}
}
