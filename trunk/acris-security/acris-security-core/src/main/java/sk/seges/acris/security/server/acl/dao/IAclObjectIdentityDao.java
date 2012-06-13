package sk.seges.acris.security.server.acl.dao;

import java.util.List;

import sk.seges.acris.security.server.core.acl.domain.api.AclSecuredObjectIdentity;
import sk.seges.sesam.dao.ICrudDAO;

public interface IAclObjectIdentityDao<T extends AclSecuredObjectIdentity> extends ICrudDAO<T> {
    
	AclSecuredObjectIdentity findByObjectId(long objectIdClass, long objectIdIdentity);
	
	AclSecuredObjectIdentity findByObjectId(long objectIdIdentity);
	
	AclSecuredObjectIdentity findById(long id);

	AclSecuredObjectIdentity createDefaultEntity();
	
	List<T> findByParent(T objectIdentity);
}
