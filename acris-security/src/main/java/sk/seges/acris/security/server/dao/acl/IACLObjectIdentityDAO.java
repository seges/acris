package sk.seges.acris.security.server.dao.acl;

import sk.seges.acris.security.server.domain.acl.ACLObjectIdentity;
import sk.seges.sesam.dao.ICrudDAO;

public interface IACLObjectIdentityDAO extends ICrudDAO<ACLObjectIdentity> {
    
	public ACLObjectIdentity findByObjectId(long objectIdClass, long objectIdIdentity);
}
