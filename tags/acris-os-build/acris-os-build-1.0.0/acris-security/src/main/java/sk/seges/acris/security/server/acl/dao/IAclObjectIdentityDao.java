package sk.seges.acris.security.server.acl.dao;

import sk.seges.acris.security.server.acl.domain.AclSecuredObjectIdentity;
import sk.seges.sesam.dao.ICrudDAO;

public interface IAclObjectIdentityDao extends ICrudDAO<AclSecuredObjectIdentity> {
    
	public AclSecuredObjectIdentity findByObjectId(long objectIdClass, long objectIdIdentity);
}
