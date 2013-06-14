package sk.seges.acris.security.server.core.user_management.service.permission;

import java.io.Serializable;
import java.util.List;

import sk.seges.acris.security.server.core.user_management.dao.permission.api.ISecurityRoleDao;
import sk.seges.acris.security.server.user_management.service.IRoleRemoteServiceLocal;
import sk.seges.corpis.server.domain.user.server.model.data.RoleData;
import sk.seges.sesam.dao.Filter;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;
import sk.seges.sesam.dao.SimpleExpression;
import sk.seges.sesam.pap.service.annotation.LocalService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@LocalService
public class SecurityRoleService extends RemoteServiceServlet implements IRoleRemoteServiceLocal {

	private static final long serialVersionUID = 6855778501707311971L;

	private ISecurityRoleDao<RoleData> securityRoleDao;

	public SecurityRoleService(ISecurityRoleDao<RoleData> securityRoleDao) {
		this.securityRoleDao = securityRoleDao;
	}

	public ISecurityRoleDao<RoleData> getRoleDao() {
		return securityRoleDao;
	}

	@Override
	public RoleData findRole(String roleName) {
		Page page = new Page(0, 0);
		SimpleExpression<Comparable<? extends Serializable>> eq = Filter.eq(RoleData.NAME);
		eq.setValue(roleName);
		page.setFilterable(eq);

		return securityRoleDao.findUnique(page);
	}

	@Override
	public RoleData persist(RoleData role) {
		return securityRoleDao.persist(role);
	}

	@Override
	public PagedResult<List<RoleData>> findAll(Page page) {
		return securityRoleDao.findAll(page);
	}

	@Override
	public RoleData merge(RoleData role) {
		return securityRoleDao.merge(role);
	}

	@Override
	public void remove(RoleData role) {
		role = securityRoleDao.findEntity(role);
		if (null != role) {
			securityRoleDao.remove(role);
		}
	}

	@Override
	public List<String> findSelectedPermissions(Integer roleId) {
		return securityRoleDao.findSelectedPermissions(roleId);
	}
}