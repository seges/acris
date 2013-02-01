package sk.seges.acris.security.server.user_management.dao.api;

import sk.seges.acris.security.server.user_management.domain.api.server.model.data.OpenIDUserData;
import sk.seges.sesam.dao.ICrudDAO;

public interface IOpenIDUserDao<E extends OpenIDUserData> extends ICrudDAO<OpenIDUserData> {
}
