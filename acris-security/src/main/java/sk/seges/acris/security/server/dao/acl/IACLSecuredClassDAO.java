package sk.seges.acris.security.server.dao.acl;

import sk.seges.acris.security.server.domain.acl.ACLSecuredClass;
import sk.seges.sesam.dao.ICrudDAO;

public interface IACLSecuredClassDAO extends ICrudDAO<ACLSecuredClass> {

    public ACLSecuredClass load(Class<?> clazz);

    public ACLSecuredClass loadOrCreate(Class<?> clazz);
}
