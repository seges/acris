package sk.seges.acris.security.server.spring.user_management.dao.permission.hibernate;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.corpis.server.domain.user.server.model.data.RoleData;
import sk.seges.sesam.dao.Page;

@Component
public class HibernateSpringSecurityRoleDao extends HibernateSecurityRoleDao {

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public List<String> findSelectedPermissions(Integer roleId) {
		return super.findSelectedPermissions(roleId);
	}

	@Override
	@Transactional
	public RoleData findByName(String name) {
		return super.findByName(name);
	}

	@Override
	@Transactional
	public RoleData findUnique(Page page) {
		return super.findUnique(page);
	}
}