package sk.seges.acris.server.model.dto.configuration;

import sk.seges.acris.core.shared.model.IDataTransferObject;
import sk.seges.acris.site.server.domain.api.ContentPkData;
import sk.seges.acris.site.server.domain.base.ContentPkBase;
import sk.seges.sesam.pap.model.annotation.*;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;

@TransferObjectMapping(domainClass = ContentPkBase.class, generateConverter = false)
@Mapping(MappingType.AUTOMATIC)
@GenerateEquals(generate = true, type = TraversalType.DEFAULT)
@GenerateHashcode(generate = true, type = TraversalType.DEFAULT)
public interface ContentPkDTOConfiguration extends IDataTransferObject {

	@TransferObjectMapping(domainClass = ContentPkData.class, configuration = ContentPkDTOConfiguration.class)
	public interface ContentPkDataDTOConfiguration {}
}