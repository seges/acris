package sk.seges.acris.security.server.dao.acl;

import org.springframework.security.acls.sid.Sid;

import sk.seges.acris.security.server.domain.acl.ACLSecurityID;
import sk.seges.sesam.dao.ICrudDAO;

public interface IACLSecurityIDDAO extends ICrudDAO<ACLSecurityID> {

    public ACLSecurityID loadOrCreate(Sid sid);
}
