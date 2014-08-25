package sk.seges.acris.security.server.core.user_management.service.permission;

import java.io.Serializable;
import java.util.List;

import sk.seges.acris.security.server.core.user_management.dao.permission.api.ISecurityRoleDao;
import sk.seges.acris.security.server.user_management.service.IRoleRemoteServiceLocal;
import sk.seges.corpis.server.domain.user.server.model.data.RoleData;
import sk.seges.sesam.dao.Conjunction;
import sk.seges.sesam.dao.Disjunction;
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
	public RoleData findRole(String roleName, String webId) {
		Page page = new Page(0, 0);
		Conjunction filterable = Filter.conjunction();
		SimpleExpression<Comparable<? extends Serializable>> filter = Filter.eq(RoleData.NAME).setValue(roleName);
		filterable.add(filter);
		filter = Filter.eq(RoleData.WEB_ID).setValue(webId);
		page.setFilterable(filterable);

		return securityRoleDao.findUnique(page);
	}

	@Override
	public RoleData persist(RoleData role) {
		return securityRoleDao.persist(role);
	}

	//Load all roles for webId and all common roles with null webId
	@Override
	public PagedResult<List<RoleData>> findAll(String webId) {
		Page page = new Page(0, Page.ALL_RESULTS);
		Disjunction filterable = Filter.disjunction();
		filterable.add(Filter.eq(RoleData.WEB_ID).setValue(webId));
		filterable.add(Filter.isNull(RoleData.WEB_ID));
		page.setFilterable(filterable);
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