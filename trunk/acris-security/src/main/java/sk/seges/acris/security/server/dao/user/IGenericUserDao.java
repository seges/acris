package sk.seges.acris.security.server.dao.user;

import sk.seges.acris.security.rpc.domain.GenericUser;
import sk.seges.acris.security.rpc.domain.IGroupAuthoritiesHolder;
import sk.seges.acris.security.rpc.domain.IUserPermission;
import sk.seges.sesam.dao.ICrudDAO;


public interface IGenericUserDao extends ICrudDAO<GenericUser> {
	GenericUser findByUsername(String username);
	GenericUser persist(GenericUser user, IGroupAuthoritiesHolder<? extends IUserPermission> authoritiesHolder);
	GenericUser persist(GenericUser user, IGroupAuthoritiesHolder<? extends IUserPermission> authoritiesHolder, boolean addMode);
	GenericUser collectUserAuthorities(GenericUser user, IGroupAuthoritiesHolder<? extends IUserPermission> authoritiesHolder, boolean addMode);
}
