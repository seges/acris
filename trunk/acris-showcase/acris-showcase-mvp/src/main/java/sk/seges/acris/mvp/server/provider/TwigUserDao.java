package sk.seges.acris.mvp.server.provider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import sk.seges.acris.security.server.user_management.dao.twig.AbstractTwigCrud;
import sk.seges.acris.security.server.user_management.dao.user.IGenericUserDao;
import sk.seges.acris.security.server.user_management.domain.twig.TwigGenericUser;
import sk.seges.acris.security.shared.user_management.domain.api.GroupAuthoritiesHolder;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.acris.security.shared.user_management.domain.api.UserDataBeanWrapper;
import sk.seges.acris.security.shared.user_management.domain.api.UserPermission;
import sk.seges.acris.security.shared.user_management.domain.api.UserRolePermission;
import sk.seges.sesam.dao.Filter;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.SimpleExpression;

import com.vercer.engine.persist.ObjectDatastore;

public class TwigUserDao extends AbstractTwigCrud<UserData> implements IGenericUserDao<UserData> {

	public TwigUserDao(ObjectDatastore datastore) {
		super(datastore, TwigGenericUser.class);
	}

	@Override
	public UserData findByUsername(String username) {
		Page page = new Page(0, Page.ALL_RESULTS);
		SimpleExpression<Comparable<? extends Serializable>> eq = Filter.eq(UserDataBeanWrapper.USERNAME);
		eq.setValue(username);
		page.setFilterable(eq);
		return super.findUnique(page);
	}

	@Override
	public UserData persist(UserData user, GroupAuthoritiesHolder<? extends UserPermission> authoritiesHolder) {
		return super.persist(collectUserAuthorities(user, authoritiesHolder, false));
	}

	@Override
	public UserData collectUserAuthorities(UserData user, GroupAuthoritiesHolder<? extends UserPermission> authoritiesHolder, boolean addMode) {
		List<String> authorities = new ArrayList<String>();

		if (addMode && user.getUserAuthorities() != null) {
			for (String authority : user.getUserAuthorities()) {
				authorities.add(authority);
			}
		}

		for (UserRolePermission rolePermission : authoritiesHolder.getRolePermissions()) {
			authorities.add("ROLE_" + rolePermission.getPermission());

			authorities.addAll(extractAuthorities(rolePermission.getUserPermissions()));
		}

		authorities.addAll(extractAuthorities(authoritiesHolder.getUserPermissions()));

		user.setUserAuthorities(authorities);

		return user;
	}

	private List<String> extractAuthorities(List<?> userPermissions) {

		List<String> userAuthorities = new ArrayList<String>();

		for (Object userPermission : userPermissions) {
			if (userPermission instanceof String) {
				userAuthorities.add((String) userPermission);
			} else if (userPermission instanceof UserPermission) {
				userAuthorities.add(((UserPermission) userPermission).name());
			}
		}

		return userAuthorities;
	}

	@Override
	public UserData persist(UserData user, GroupAuthoritiesHolder<? extends UserPermission> authoritiesHolder, boolean addMode) {
		return super.persist(collectUserAuthorities(user, authoritiesHolder, false));
	}
}