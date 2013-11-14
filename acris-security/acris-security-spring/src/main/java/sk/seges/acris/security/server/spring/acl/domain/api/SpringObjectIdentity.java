package sk.seges.acris.security.server.spring.acl.domain.api;

import org.springframework.security.acls.model.ObjectIdentity;

import sk.seges.acris.security.acl.server.model.data.AclSecuredObjectIdentityData;

public interface SpringObjectIdentity extends AclSecuredObjectIdentityData, ObjectIdentity {
}