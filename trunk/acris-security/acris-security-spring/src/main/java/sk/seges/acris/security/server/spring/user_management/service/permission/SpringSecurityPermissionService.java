package sk.seges.acris.security.server.spring.user_management.service.permission;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.server.core.user_management.dao.permission.api.ISecurityPermissionDao;
import sk.seges.acris.security.server.core.user_management.service.permission.SecurityPermissionService;
import sk.seges.acris.security.user_management.server.model.data.HierarchyPermissionData;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

@Service
public class SpringSecurityPermissionService extends SecurityPermissionService {

	@Autowired
	public SpringSecurityPermissionService(@Qualifier(value = "securityPermissionDao") ISecurityPermissionDao<HierarchyPermissionData> securityPermissionDao) {
		super(securityPermissionDao);
	}

	@Override
	@Transactional
	public PagedResult<List<HierarchyPermissionData>> findAll(String webId, Page page) {
		return super.findAll(webId, page);
	}

	@Override
	@Transactional
	public void persist(HierarchyPermissionData rolePermission) {
		super.persist(rolePermission);
	}

	@Override
	@Transactional
	public void remove(HierarchyPermissionData rolePermission) {
		super.remove(rolePermission);
	}

	@Override
	@Transactional
	public List<HierarchyPermissionData> findSecurityPermissions(Integer parentId, String webId) {
		return super.findSecurityPermissions(parentId, webId);
	}
}
