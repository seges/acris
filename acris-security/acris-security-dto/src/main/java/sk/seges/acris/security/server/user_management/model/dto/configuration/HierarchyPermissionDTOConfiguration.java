package sk.seges.acris.security.server.user_management.model.dto.configuration;

import sk.seges.acris.core.client.rpc.IDataTransferObject;
import sk.seges.acris.security.server.core.user_management.domain.jpa.JpaSecurityPermission;
import sk.seges.acris.security.user_management.server.model.data.HierarchyPermissionData;
import sk.seges.sesam.pap.model.annotation.GenerateEquals;
import sk.seges.sesam.pap.model.annotation.GenerateHashcode;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

@TransferObjectMapping(domainClass = JpaSecurityPermission.class)
@Mapping(MappingType.AUTOMATIC)
@GenerateEquals(generate = false)
@GenerateHashcode(generate = false)
public interface HierarchyPermissionDTOConfiguration extends IDataTransferObject {
	
	@TransferObjectMapping(domainClass = HierarchyPermissionData.class, configuration = HierarchyPermissionDTOConfiguration.class)
	public interface HierarchicPermissionDataConfiguration {}

}