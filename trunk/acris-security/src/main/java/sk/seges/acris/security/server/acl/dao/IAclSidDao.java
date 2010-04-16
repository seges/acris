package sk.seges.acris.security.server.acl.dao;

import org.springframework.security.acls.sid.Sid;

import sk.seges.acris.security.server.acl.domain.AclSid;
import sk.seges.sesam.dao.ICrudDAO;

public interface IAclSidDao extends ICrudDAO<AclSid> {

    public AclSid loadOrCreate(Sid sid);
}
