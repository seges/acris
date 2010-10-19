package sk.seges.acris.security.server.acl.dao;

import sk.seges.acris.security.server.core.acl.domain.api.AclSid;
import sk.seges.sesam.dao.ICrudDAO;

public interface IAclSidDao<T extends AclSid> extends ICrudDAO<T> {

    public AclSid loadOrCreate(String sidName, boolean principal);
}
