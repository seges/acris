package sk.seges.acris.security.server.spring.acl.domain.api;

import org.springframework.security.acls.objectidentity.ObjectIdentity;

import sk.seges.acris.security.server.core.acl.domain.api.AclSecuredObjectIdentity;

public interface SpringObjectIdentity extends AclSecuredObjectIdentity, ObjectIdentity {
}