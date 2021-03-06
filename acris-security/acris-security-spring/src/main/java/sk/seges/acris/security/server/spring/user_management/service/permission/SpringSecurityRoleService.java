package sk.seges.acris.security.server.spring.user_management.service.permission;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.server.core.user_management.dao.permission.api.ISecurityRoleDao;
import sk.seges.acris.security.server.core.user_management.service.permission.SecurityRoleService;
import sk.seges.corpis.server.domain.user.server.model.data.RoleData;
import sk.seges.sesam.dao.PagedResult;

// FIXME
@Service
public class SpringSecurityRoleService extends SecurityRoleService {

	private static final long serialVersionUID = 7406180807542840166L;

	@Autowired
	public SpringSecurityRoleService(ISecurityRoleDao<RoleData> securityRoleDao) {
		super(securityRoleDao);
	}

	@Override
	@Transactional
	public RoleData findRole(String roleName, String webId) {
		return super.findRole(roleName, webId);
	}

	@Override
	@Transactional
	public RoleData persist(RoleData role) {
		return super.persist(role);
	}

	@Override
	@Transactional
	public PagedResult<List<RoleData>> findAll(String webId) {
		return super.findAll(webId);
	}

	@Override
	@Transactional
	public RoleData merge(RoleData role) {
		return super.merge(role);
	}

	@Override
	@Transactional
	public void remove(RoleData role) {
		super.remove(role);
	}

	@Override
	@Transactional
	public List<String> findSelectedPermissions(Integer roleId) {
		return super.findSelectedPermissions(roleId);
	}
}