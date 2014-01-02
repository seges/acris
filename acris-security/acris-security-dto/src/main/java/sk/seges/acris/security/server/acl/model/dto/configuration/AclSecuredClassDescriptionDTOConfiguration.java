package sk.seges.acris.security.server.acl.model.dto.configuration;

import sk.seges.acris.core.client.rpc.IDataTransferObject;
import sk.seges.acris.security.acl.server.model.data.AclSecuredClassDescriptionData;
import sk.seges.acris.security.core.server.acl.domain.jpa.JpaAclSecuredClassDescription;
import sk.seges.sesam.pap.model.annotation.GenerateEquals;
import sk.seges.sesam.pap.model.annotation.GenerateHashcode;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

@TransferObjectMapping(domainClass = JpaAclSecuredClassDescription.class)
@GenerateEquals(generate = false)
@GenerateHashcode(generate = false)
@Mapping(Mapping.MappingType.EXPLICIT)
public interface AclSecuredClassDescriptionDTOConfiguration extends IDataTransferObject {

	@TransferObjectMapping(domainClass = AclSecuredClassDescriptionData.class, configuration = AclSecuredClassDescriptionDTOConfiguration.class)
	public interface AclClassDescriptionConfiguration {}

	void id();
}