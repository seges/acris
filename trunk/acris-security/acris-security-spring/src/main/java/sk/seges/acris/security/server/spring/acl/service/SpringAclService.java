package sk.seges.acris.security.server.spring.acl.service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.acls.AccessControlEntry;
import org.springframework.security.acls.Acl;
import org.springframework.security.acls.AclService;
import org.springframework.security.acls.NotFoundException;
import org.springframework.security.acls.Permission;
import org.springframework.security.acls.domain.AccessControlEntryImpl;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.domain.AclImpl;
import org.springframework.security.acls.domain.AuditLogger;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.jdbc.AclCache;
import org.springframework.security.acls.objectidentity.ObjectIdentity;
import org.springframework.security.acls.sid.GrantedAuthoritySid;
import org.springframework.security.acls.sid.PrincipalSid;
import org.springframework.security.acls.sid.Sid;
import org.springframework.security.util.FieldUtils;

import sk.seges.acris.security.acl.server.model.data.AclEntryData;
import sk.seges.acris.security.acl.server.model.data.AclSecuredClassDescriptionData;
import sk.seges.acris.security.acl.server.model.data.AclSecuredObjectIdentityData;
import sk.seges.acris.security.acl.server.model.data.AclSidData;
import sk.seges.acris.security.server.core.acl.dao.api.IAclObjectIdentityDao;
import sk.seges.acris.security.server.core.acl.dao.api.IAclRecordDao;
import sk.seges.acris.security.server.core.acl.dao.api.IAclSecuredClassDescriptionDao;
import sk.seges.acris.security.server.core.acl.dao.api.IAclSidDao;

public class SpringAclService implements AclService {

    @Autowired
    protected AclAuthorizationStrategy aclAuthorizationStrategy;
    
    protected AclCache aclCache;
    
    @Autowired
    protected AuditLogger auditLogger;

	@Autowired
	@Qualifier(value = "aclRecordDao")
	protected IAclRecordDao<AclEntryData> aclEntryDao;

	@Autowired
	protected IAclSidDao<AclSidData> aclSecurityIDDao;

	@Autowired
	protected IAclSecuredClassDescriptionDao<AclSecuredClassDescriptionData> aclSecuredClassDao;

	@Autowired
	protected IAclObjectIdentityDao<AclSecuredObjectIdentityData> aclObjectIdentityDao;

	public SpringAclService(AclCache aclCache) {
		this.aclCache = aclCache;
	}
	
    public ObjectIdentity[] findChildren(ObjectIdentity parentIdentity) {
        // TODO Auto-generated method stub
    	//FIXME
        return null;
    }

    public Acl readAclById(ObjectIdentity object) throws NotFoundException {
        return readAclById(object, null);
    }

    public Acl readAclById(ObjectIdentity object, Sid[] sids)
            throws NotFoundException {
        Map<ObjectIdentity, Acl> map = readAclsById(new ObjectIdentity[] {object}, sids);
        return (Acl) map.get(object);
    }

    public Map<ObjectIdentity, Acl> readAclsById(ObjectIdentity[] objects) throws NotFoundException {
        return readAclsById(objects, null);
    }

    @SuppressWarnings("unchecked")
    public Map<ObjectIdentity, Acl> readAclsById(ObjectIdentity[] objects, Sid[] sids)
            throws NotFoundException {
        final Map<ObjectIdentity, Acl> acls = new HashMap<ObjectIdentity, Acl>();
        
        for (ObjectIdentity object :objects){

        	Acl aclFromCache = aclCache.getFromCache(object);

        	if (aclFromCache != null) {
        		acls.put(object, aclFromCache);
        		continue;
        	}

        	AclSecuredClassDescriptionData aclClass = aclSecuredClassDao.load(object.getJavaType());
            
            if (aclClass == null) {
            	//There is for sure no ACL entries for this object identity
            	throw new NotFoundException("Could not found specified aclObjectIdentity.");
            }
            // No need to check for nulls, as guaranteed non-null by ObjectIdentity.getIdentifier() interface contract
            String identifier = object.getIdentifier().toString();
            long id = (Long.valueOf(identifier)).longValue();
            AclSecuredObjectIdentityData aclObjectIdentity = aclObjectIdentityDao.findByObjectId(aclClass.getId(), id);

            if(aclObjectIdentity==null){
                throw new NotFoundException("Could not found specified aclObjectIdentity.");
            }

            AclSidData aclOwnerSid = aclObjectIdentity.getSid();
            Sid owner;

            if (aclOwnerSid.isPrincipal()) {
                owner = new PrincipalSid(aclOwnerSid.getSid());
            } else {
                owner = new GrantedAuthoritySid(aclOwnerSid.getSid());
            }
            
            AclImpl acl = new AclImpl(object, aclObjectIdentity.getId(), 
                                        aclAuthorizationStrategy, auditLogger, 
                                        null, null, false, owner);
            acls.put(object, acl); 
            
            aclCache.putInCache(acl);

            Field acesField = FieldUtils.getField(AclImpl.class, "aces");
            List<AccessControlEntry> aces;

            try {
                acesField.setAccessible(true);
                aces = (List<AccessControlEntry>) acesField.get(acl);
            } catch (IllegalAccessException ex) {
                throw new IllegalStateException("Could not obtain AclImpl.ace field: cause[" + ex.getMessage() + "]");
            }
            
            List<AclEntryData> aclEntrys = loadAclEntries(aclObjectIdentity);
            
            for(AclEntryData aclEntry:aclEntrys){
                AclSidData aclSid = aclEntry.getSid();
                Sid recipient;
                if (aclSid.isPrincipal()) {
                    recipient = new PrincipalSid(aclSid.getSid());
                } else {
                    recipient = new GrantedAuthoritySid(aclSid.getSid());
                }  
                
                int mask = aclEntry.getMask();
                Permission permission = convertMaskIntoPermission(mask);
                boolean granting = aclEntry.isGranting();
                boolean auditSuccess = aclEntry.isAuditSuccess();
                boolean auditFailure = aclEntry.isAuditFailure();       
                
                AccessControlEntryImpl ace = new AccessControlEntryImpl(aclEntry.getId(), acl, recipient, permission, granting,
                        auditSuccess, auditFailure);       
                
                // Add the ACE if it doesn't already exist in the ACL.aces field
                 if (!aces.contains(ace)) {
                     aces.add(ace);
                 }                   
            }
       
        }
        return acls;
    }
    
    private List<AclEntryData> loadAclEntries(AclSecuredObjectIdentityData aclObjectIdentity) {
    	List<AclEntryData> entries = getEntries(aclObjectIdentity.getId());   
    	while (aclObjectIdentity.getParentObject() != null) {
    		aclObjectIdentity = aclObjectIdentity.getParentObject();
    		entries.addAll(getEntries(aclObjectIdentity.getId()));
    	}
    	return entries;
    }
    
	private List<AclEntryData> getEntries(Long objectIdentityId) {
    	return aclEntryDao.findByIdentityId(objectIdentityId);
    }
    
    protected Permission convertMaskIntoPermission(int mask) {
    	//TODO, Add extended permission
        return BasePermission.buildFromMask(mask);
    }

	public void setAclCache(AclCache aclCache) {
		this.aclCache = aclCache;
	}
}