package sk.seges.acris.security.server.acl.dao;

import sk.seges.acris.security.server.core.acl.domain.api.AclSecuredClassDescription;
import sk.seges.sesam.dao.ICrudDAO;

public interface IAclSecuredClassDescriptionDao<T extends AclSecuredClassDescription> extends ICrudDAO<T> {

    public AclSecuredClassDescription load(Class<?> clazz);

    public AclSecuredClassDescription loadOrCreate(Class<?> clazz);
}
