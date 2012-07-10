package sk.seges.acris.security.server.acl.dao;

import java.util.List;

import sk.seges.acris.security.server.core.acl.domain.api.AclSecuredObjectIdentity;
import sk.seges.acris.security.shared.domain.ISecuredObject;
import sk.seges.sesam.dao.ICrudDAO;

public interface IAclObjectIdentityDao<T extends AclSecuredObjectIdentity> extends ICrudDAO<T> {
    
	AclSecuredObjectIdentity findByObjectId(long objectIdClass, long objectIdIdentity);
	AclSecuredObjectIdentity findByObjectId(Class<? extends ISecuredObject<?>> objectClass, long objectIdIdentity);

	AclSecuredObjectIdentity findById(long id);

	AclSecuredObjectIdentity createDefaultEntity();
	
	List<T> findByParent(T objectIdentity);
}
