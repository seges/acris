package sk.seges.acris.security.server.core.acl.dao.api;

import java.util.List;

import sk.seges.acris.security.acl.server.model.data.AclEntryData;
import sk.seges.acris.security.acl.server.model.data.AclSidData;
import sk.seges.sesam.dao.ICrudDAO;
import sk.seges.sesam.domain.IDomainObject;

public interface IAclRecordDao<T extends AclEntryData> extends ICrudDAO<T> {

    List<AclEntryData> findByIdentityId(long aclObjectIdentity);
    
    List<AclEntryData> findByClassnameAndSid(Class<? extends IDomainObject<?>> securedClass, AclSidData sid);

    void deleteByClassnameAndSid(Class<? extends IDomainObject<?>> securedClass, AclSidData sid);
    
    void deleteByIdentityIdAndSid(Long aclId, Class<? extends IDomainObject<?>> clazz, AclSidData sid);
    
    void deleteByIdentityIdAndSid(Long aclId, Class<? extends IDomainObject<?>> clazz, AclSidData sid, String className);

	void remove(AclEntryData aclEntry);

    void deleteByIdentityId(long aclObjectId);
    
    AclEntryData createDefaultEntity();
}