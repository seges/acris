package sk.seges.acris.security.server.core.user_management.dao.user;

import sk.seges.acris.security.shared.user_management.domain.api.GroupAuthoritiesHolder;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.acris.security.shared.user_management.domain.api.UserPermission;
import sk.seges.sesam.dao.ICrudDAO;

public interface IGenericUserDao<T extends UserData<?>> extends ICrudDAO<T> {

	T findByUsername(String username);

	T persist(T user, GroupAuthoritiesHolder<? extends UserPermission> authoritiesHolder);

	T persist(T user, GroupAuthoritiesHolder<? extends UserPermission> authoritiesHolder, boolean addMode);

	T collectUserAuthorities(T user, GroupAuthoritiesHolder<? extends UserPermission> authoritiesHolder, boolean addMode);
}
