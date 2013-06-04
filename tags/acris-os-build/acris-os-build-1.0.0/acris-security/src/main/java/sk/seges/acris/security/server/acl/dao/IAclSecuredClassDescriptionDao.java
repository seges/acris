package sk.seges.acris.security.server.acl.dao;

import sk.seges.acris.security.server.acl.domain.AclSecuredClassDescription;
import sk.seges.sesam.dao.ICrudDAO;

public interface IAclSecuredClassDescriptionDao extends ICrudDAO<AclSecuredClassDescription> {

    public AclSecuredClassDescription load(Class<?> clazz);

    public AclSecuredClassDescription loadOrCreate(Class<?> clazz);
}
