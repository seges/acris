package sk.seges.acris.security.server.core.acl.dao.api;

import sk.seges.acris.security.acl.server.model.data.AclSecuredClassDescriptionData;
import sk.seges.sesam.dao.ICrudDAO;

public interface IAclSecuredClassDescriptionDao<T extends AclSecuredClassDescriptionData> extends ICrudDAO<T> {

    AclSecuredClassDescriptionData load(Class<?> clazz);

    AclSecuredClassDescriptionData loadOrCreate(Class<?> clazz);
    
    AclSecuredClassDescriptionData load(String className);

    AclSecuredClassDescriptionData loadOrCreate(String className);
}