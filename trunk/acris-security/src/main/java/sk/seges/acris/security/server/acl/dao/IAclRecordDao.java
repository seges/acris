package sk.seges.acris.security.server.acl.dao;

import java.util.List;

import org.springframework.security.acls.sid.Sid;

import sk.seges.acris.security.rpc.domain.ISecuredObject;
import sk.seges.acris.security.server.acl.domain.AclEntry;
import sk.seges.sesam.dao.ICrudDAO;

public interface IAclRecordDao extends ICrudDAO<AclEntry> {

    public List<AclEntry> findByIdentityId(long aclObjectIdentity);
    
    public List<AclEntry> findByClassnameAndSid(Class<? extends ISecuredObject> securedClass, Sid sid);

    public void deleteByClassnameAndSid(Class<? extends ISecuredObject> securedClass, Sid sid);
    
    public void deleteByIdentityIdAndSid(ISecuredObject securedObject, Sid sid);

	public void remove(AclEntry aclEntry);

    public void deleteByIdentityId(long aclObjectId);

}
