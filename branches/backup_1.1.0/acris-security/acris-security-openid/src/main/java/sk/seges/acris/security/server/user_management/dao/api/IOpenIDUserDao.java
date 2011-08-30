package sk.seges.acris.security.server.user_management.dao.api;

import sk.seges.acris.security.shared.user_management.domain.api.HasOpenIDIdentifier;
import sk.seges.sesam.dao.ICrudDAO;

public interface IOpenIDUserDao<E extends HasOpenIDIdentifier> extends ICrudDAO<HasOpenIDIdentifier> {
}
