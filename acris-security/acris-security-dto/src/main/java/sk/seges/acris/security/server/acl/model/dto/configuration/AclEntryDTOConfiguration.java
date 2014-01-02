package sk.seges.acris.security.server.acl.model.dto.configuration;

import sk.seges.acris.core.client.rpc.IDataTransferObject;
import sk.seges.acris.security.acl.server.model.data.AclEntryData;
import sk.seges.acris.security.core.server.acl.domain.jpa.JpaAclEntry;
import sk.seges.sesam.pap.model.annotation.GenerateEquals;
import sk.seges.sesam.pap.model.annotation.GenerateHashcode;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

@TransferObjectMapping(domainClass = JpaAclEntry.class)
@GenerateEquals(generate = false)
@GenerateHashcode(generate = false)
@Mapping(Mapping.MappingType.EXPLICIT)
public interface AclEntryDTOConfiguration extends IDataTransferObject {

	@TransferObjectMapping(domainClass = AclEntryData.class, configuration = AclEntryDTOConfiguration.class)
	public interface AclEntryDataConfiguration {}

	void id();
}