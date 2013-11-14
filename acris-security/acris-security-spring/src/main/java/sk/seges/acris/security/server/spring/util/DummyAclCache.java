package sk.seges.acris.security.server.spring.util;

import java.io.Serializable;

import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.ObjectIdentity;

public class DummyAclCache implements AclCache {

	public void evictFromCache(Serializable pk) {
	}

	public void evictFromCache(ObjectIdentity objectIdentity) {
	}

	public MutableAcl getFromCache(ObjectIdentity objectIdentity) {
		return null;
	}

	public MutableAcl getFromCache(Serializable pk) {
		return null;
	}

	public void putInCache(MutableAcl acl) {
	}

	@Override
	public void clearCache() {
	}
}
