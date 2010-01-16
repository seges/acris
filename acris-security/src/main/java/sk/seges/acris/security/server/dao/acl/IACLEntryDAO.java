package sk.seges.acris.security.server.dao.acl;

import java.util.List;

import org.springframework.security.acls.sid.Sid;

import sk.seges.acris.security.rpc.domain.ISecuredObject;
import sk.seges.acris.security.server.domain.acl.ACLEntry;
import sk.seges.sesam.dao.ICrudDAO;

public interface IACLEntryDAO extends ICrudDAO<ACLEntry> {

    public List<ACLEntry> findByIdentityId(long aclObjectIdentity);

    public void deleteByIdentityIdAndSid(ISecuredObject securedObject, Sid sid);

	public void remove(ACLEntry aclEntry);

    public void deleteByIdentityId(long aclObjectId);

}
