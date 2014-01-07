package sk.seges.acris.server.model.dto.configuration;

import sk.seges.acris.core.client.rpc.IDataTransferObject;
import sk.seges.acris.site.server.domain.api.ContentPkData;
import sk.seges.acris.site.server.domain.base.ContentPkBase;
import sk.seges.sesam.pap.model.annotation.GenerateEquals;
import sk.seges.sesam.pap.model.annotation.GenerateHashcode;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

@TransferObjectMapping(domainClass = ContentPkBase.class, generateConverter = false)
@Mapping(MappingType.AUTOMATIC)
@GenerateEquals(generate = false)
@GenerateHashcode(generate = false)
public interface ContentPkDTOConfiguration extends IDataTransferObject {

	@TransferObjectMapping(domainClass = ContentPkData.class, configuration = ContentPkDTOConfiguration.class)
	public interface ContentPkDataDTOConfiguration {}
}