package sk.seges.acris.security.server.acl.service;

import org.springframework.security.Authentication;
import org.springframework.security.acls.AccessControlEntry;
import org.springframework.security.acls.Acl;
import org.springframework.security.acls.AlreadyExistsException;
import org.springframework.security.acls.ChildrenExistException;
import org.springframework.security.acls.MutableAcl;
import org.springframework.security.acls.MutableAclService;
import org.springframework.security.acls.NotFoundException;
import org.springframework.security.acls.Permission;
import org.springframework.security.acls.domain.AccessControlEntryImpl;
import org.springframework.security.acls.objectidentity.ObjectIdentity;
import org.springframework.security.acls.objectidentity.ObjectIdentityImpl;
import org.springframework.security.acls.sid.PrincipalSid;
import org.springframework.security.acls.sid.Sid;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.rpc.domain.ISecuredObject;
import sk.seges.acris.security.server.acl.domain.AclSid;
import sk.seges.acris.security.server.acl.domain.AclEntry;
import sk.seges.acris.security.server.acl.domain.AclSecuredClassDescription;
import sk.seges.acris.security.server.acl.domain.AclSecuredObjectIdentity;


@Service
public class HibernateMutableAclService extends HibernateAclService implements MutableAclService {

    @Transactional
	public MutableAcl createAcl(ObjectIdentity objectIdentity)
			throws AlreadyExistsException {

		AclSecuredObjectIdentity aclObjectIdentity = getAclSecuredObjectIdentity(objectIdentity);
		// Check this object identity hasn't already been persisted
		if (aclObjectIdentity != null) {
			throw new AlreadyExistsException("Object identity '" + aclObjectIdentity + "' already exists");
		}

		AclSecuredClassDescription aclClass = aclSecuredClassDao.loadOrCreate(objectIdentity.getJavaType());

		// Need to retrieve the current principal, in order to know who "owns"
		// this ACL (can be changed later on)
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		PrincipalSid sid = new PrincipalSid(auth);

		AclSid aclSid = aclSecurityIDDao.loadOrCreate(sid);

		aclObjectIdentity = new AclSecuredObjectIdentity();
		aclObjectIdentity.setObjectIdClass(aclClass);
		aclObjectIdentity.setSid(aclSid);

		aclObjectIdentity.setObjectIdIdentity(Long.parseLong((objectIdentity
				.getIdentifier().toString())));
		aclObjectIdentity.setEntriesInheriting(true);
		aclObjectIdentityDao.persist(aclObjectIdentity);

		// Retrieve the ACL via superclass (ensures cache registration, proper
		// retrieval etc)
		Acl acl = readAclById(objectIdentity);
		return (MutableAcl) acl;
	}

	protected void createRecords(final MutableAcl acl) {
		int i = 1;
		for (AccessControlEntry entry_ : acl.getEntries()) {
			AccessControlEntryImpl entry = (AccessControlEntryImpl) entry_;
			AclEntry aclEntry = new AclEntry();
			long oid = ((Long) acl.getId()).longValue();
			AclSecuredObjectIdentity tempObjectIdentity = new AclSecuredObjectIdentity(); //TODO add findEntityById
			tempObjectIdentity.setId(oid);
			aclEntry.setObjectIdentity(aclObjectIdentityDao.findEntity(tempObjectIdentity));
			aclEntry.setAceOrder(i);
			AclSid aclSid = aclSecurityIDDao.loadOrCreate(entry.getSid());
			aclEntry.setSid(aclSid);
			aclEntry.setAuditFailure(entry.isAuditFailure());
			aclEntry.setAuditSuccess(entry.isAuditSuccess());
			aclEntry.setGranting(entry.isGranting());
			aclEntry.setMask(entry.getPermission().getMask());
			aclEntryDao.persist(aclEntry);
			i++;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.acls.MutableAclService#deleteAcl(org.springframework.security.acls.objectidentity.ObjectIdentity,
	 *      boolean)
	 */
    @Transactional
	public void deleteAcl(ObjectIdentity objectIdentity, boolean deleteChildren)
			throws ChildrenExistException {
		AclSecuredClassDescription aclClass = aclSecuredClassDao.load(objectIdentity.getJavaType());
		// No need to check for nulls, as guaranteed non-null by
		// ObjectIdentity.getIdentifier() interface contract
		String identifier = objectIdentity.getIdentifier().toString();
		long id = (Long.valueOf(identifier)).longValue();
		AclSecuredObjectIdentity aclObjectIdentity = aclObjectIdentityDao
				.findByObjectId(aclClass.getId(), id);
		aclEntryDao.deleteByIdentityId(aclObjectIdentity.getId());
		aclObjectIdentityDao.remove(aclObjectIdentity);
		aclCache.evictFromCache(objectIdentity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.acls.MutableAclService#updateAcl(org.springframework.security.acls.MutableAcl)
	 */
    @Transactional
	public MutableAcl updateAcl(MutableAcl acl) throws NotFoundException {
		// Delete this ACL's ACEs in the acl_entry table
		// long oid = ((Long) acl.getId()).longValue();
		AclSecuredObjectIdentity aclo = getAclSecuredObjectIdentity(acl.getObjectIdentity());
		aclEntryDao.deleteByIdentityId(aclo.getId());
		aclCache.evictFromCache(acl.getObjectIdentity());
		// Create this ACL's ACEs in the acl_entry table
		createRecords(acl);

		// Retrieve the ACL via superclass (ensures cache registration, proper
		// retrieval etc)
		return (MutableAcl) readAclById(acl.getObjectIdentity());
	}

    @Transactional
	public AclSecuredObjectIdentity getAclSecuredObjectIdentity(ObjectIdentity objectIdentity) {
		AclSecuredClassDescription aclClass = aclSecuredClassDao.loadOrCreate(objectIdentity.getJavaType());

		// No need to check for nulls, as guaranteed non-null by
		// ObjectIdentity.getIdentifier() interface contract
		String identifier = objectIdentity.getIdentifier().toString();
		long id = (Long.valueOf(identifier)).longValue();
		AclSecuredObjectIdentity aclObjectIdentity = aclObjectIdentityDao
				.findByObjectId(aclClass.getId(), id);
		return aclObjectIdentity;

	}


	public void addPermission(ISecuredObject secureObject,
			Permission permission, Class<?> clazz) {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();

		Sid recipient;
		if (auth.getPrincipal() instanceof UserDetails) {
			recipient = new PrincipalSid(((UserDetails) auth.getPrincipal())
					.getUsername());
		} else {
			recipient = new PrincipalSid(auth.getPrincipal().toString());
		}

		addPermission(secureObject, recipient, permission, clazz);
	}

	public void addPermission(ISecuredObject securedObject,
			Sid recipient, Permission permission, Class<?> clazz) {
		MutableAcl acl;

		ObjectIdentity oid = new ObjectIdentityImpl(clazz.getCanonicalName(),
				securedObject.getId());

		try {
			acl = (MutableAcl) readAclById(oid);
		} catch (NotFoundException nfe) {
			acl = createAcl(oid);
		}

		acl.insertAce(acl.getEntries().length, permission, recipient, true);
		updateAcl(acl);
	}
}