package sk.seges.acris.security.server.acl.model.dto.configuration;

import sk.seges.acris.core.client.rpc.IDataTransferObject;
import sk.seges.acris.security.acl.server.model.data.AclSidData;
import sk.seges.acris.security.core.server.acl.domain.jpa.JpaAclSid;
import sk.seges.sesam.pap.model.annotation.GenerateEquals;
import sk.seges.sesam.pap.model.annotation.GenerateHashcode;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

@TransferObjectMapping(domainClass = JpaAclSid.class)
@GenerateEquals(generate = false)
@GenerateHashcode(generate = false)
@Mapping(Mapping.MappingType.EXPLICIT)
public interface AclSidDTOConfiguration extends IDataTransferObject {

	@TransferObjectMapping(domainClass = AclSidData.class, configuration = AclSidDTOConfiguration.class)
	public interface AclSidDataConfiguration {}

	void id();
}