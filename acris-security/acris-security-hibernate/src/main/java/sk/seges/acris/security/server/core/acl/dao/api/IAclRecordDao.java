package sk.seges.acris.security.server.core.acl.dao.api;

import sk.seges.acris.security.acl.server.model.data.AclEntryData;
import sk.seges.acris.security.acl.server.model.data.AclSidData;
import sk.seges.sesam.dao.ICrudDAO;
import sk.seges.sesam.security.shared.domain.ISecuredObject;

import java.util.List;

public interface IAclRecordDao<T extends AclEntryData> extends ICrudDAO<T> {

    List<AclEntryData> findByIdentityId(long aclObjectIdentity);
    
    List<AclEntryData> findByClassnameAndSid(Class<? extends ISecuredObject<?>> securedClass, AclSidData sid);

    void deleteByClassnameAndSid(Class<? extends ISecuredObject<?>> securedClass, AclSidData sid);
    
    void deleteByIdentityIdAndSid(Long aclId, Class<? extends ISecuredObject<?>> clazz, AclSidData sid);
    
    void deleteByIdentityIdAndSid(Long aclId, Class<? extends ISecuredObject<?>> clazz, AclSidData sid, String className);

	void remove(AclEntryData aclEntry);

    void deleteByIdentityId(long aclObjectId);
    
    AclEntryData createDefaultEntity();
}