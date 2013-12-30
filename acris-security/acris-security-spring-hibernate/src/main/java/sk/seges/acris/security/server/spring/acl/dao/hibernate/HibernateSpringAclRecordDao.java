package sk.seges.acris.security.server.spring.acl.dao.hibernate;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.acl.server.model.data.AclEntryData;
import sk.seges.acris.security.acl.server.model.data.AclSidData;
import sk.seges.acris.security.server.core.acl.dao.hibernate.HibernateAclRecordDao;
import sk.seges.sesam.security.shared.domain.ISecuredObject;

@Repository
@Qualifier(value = "aclRecordDao")
public class HibernateSpringAclRecordDao extends HibernateAclRecordDao {

	@Override
	@Transactional
    public List<AclEntryData> findByIdentityId(long aclObjectIdentity) {
		return super.findByIdentityId(aclObjectIdentity);
    }

	@Override
	@Transactional
    public void deleteByIdentityIdAndSid(Long aclId, Class<? extends ISecuredObject<?>> clazz, AclSidData sid) {
		super.deleteByIdentityIdAndSid(aclId, clazz, sid);
	}
	
	@Override
	@Transactional
    public void deleteByIdentityIdAndSid(Long aclId, Class<? extends ISecuredObject<?>> clazz, AclSidData sid, String className) {
		super.deleteByIdentityIdAndSid(aclId, clazz, sid, className);
    }

	@Override
    @Transactional
	public void deleteByClassnameAndSid(Class<? extends ISecuredObject<?>> securedClass, AclSidData sid) {
		super.deleteByClassnameAndSid(securedClass, sid);
	}

	@Override
    @Transactional
    public List<AclEntryData> findByClassnameAndSid(Class<? extends ISecuredObject<?>> securedClass, AclSidData sid) {
		return super.findByClassnameAndSid(securedClass, sid);
    }
    
	@Override
	@Transactional
	public void remove(AclEntryData aclEntry) {
		super.remove(aclEntry);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteByIdentityId(long aclObjectId) {
		super.deleteByIdentityId(aclObjectId);
    }
}