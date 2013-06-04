package sk.seges.acris.security.server.core.acl.dao.api;

import java.util.List;

import sk.seges.acris.security.acl.server.model.data.AclSecuredObjectIdentityData;
import sk.seges.sesam.dao.ICrudDAO;

public interface IAclObjectIdentityDao<T extends AclSecuredObjectIdentityData> extends ICrudDAO<T> {
    
	AclSecuredObjectIdentityData findByObjectId(long objectIdClass, long objectIdIdentity);

	AclSecuredObjectIdentityData findById(long id);

	AclSecuredObjectIdentityData createDefaultEntity();
	
	List<T> findByParent(T objectIdentity);
}
