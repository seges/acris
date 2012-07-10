package sk.seges.acris.security.server.acl.dao;

import java.util.List;

import sk.seges.acris.security.server.core.acl.domain.api.AclEntry;
import sk.seges.acris.security.server.core.acl.domain.api.AclSid;
import sk.seges.acris.security.shared.domain.ISecuredObject;
import sk.seges.sesam.dao.ICrudDAO;

public interface IAclRecordDao<T extends AclEntry> extends ICrudDAO<T> {

    List<AclEntry> findByIdentityId(long aclObjectIdentity);
    
    List<AclEntry> findByClassnameAndSid(Class<? extends ISecuredObject<?>> securedClass, AclSid sid);

    void deleteByClassnameAndSid(Class<? extends ISecuredObject<?>> securedClass, AclSid sid);
    
    void deleteByIdentityIdAndSid(Long aclId, Class<? extends ISecuredObject<?>> clazz, AclSid sid);
    
    void deleteByIdentityIdAndSid(Long aclId, Class<? extends ISecuredObject<?>> clazz, AclSid sid, String className);

	void remove(AclEntry aclEntry);

    void deleteByIdentityId(long aclObjectId);
    
    AclEntry createDefaultEntity();
}