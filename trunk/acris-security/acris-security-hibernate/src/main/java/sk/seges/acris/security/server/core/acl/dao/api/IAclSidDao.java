package sk.seges.acris.security.server.core.acl.dao.api;

import sk.seges.acris.security.acl.server.model.data.AclSidData;
import sk.seges.sesam.dao.ICrudDAO;

public interface IAclSidDao<T extends AclSidData> extends ICrudDAO<T> {

    AclSidData loadOrCreate(String sidName, boolean principal);
}