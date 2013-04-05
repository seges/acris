package sk.seges.acris.security.server.acl.model.dto.configuration;

import sk.seges.acris.core.client.rpc.IDataTransferObject;
import sk.seges.acris.security.acl.server.model.data.AclSecuredObjectIdentityData;
import sk.seges.acris.security.core.server.acl.domain.jpa.JpaAclSecuredObjectIdentity;
import sk.seges.sesam.pap.model.annotation.GenerateEquals;
import sk.seges.sesam.pap.model.annotation.GenerateHashcode;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

@TransferObjectMapping(domainClass = JpaAclSecuredObjectIdentity.class)
@GenerateEquals(generate = false)
@GenerateHashcode(generate = false)
public interface AclSecuredObjectIdentityDTOConfiguration extends IDataTransferObject {

	@TransferObjectMapping(domainClass = AclSecuredObjectIdentityData.class, configuration = AclSecuredObjectIdentityDTOConfiguration.class)
	public interface AclSecuredObjectIdentityDataConfiguration {}

}