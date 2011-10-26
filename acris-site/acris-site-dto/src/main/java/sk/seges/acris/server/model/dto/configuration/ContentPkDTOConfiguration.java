package sk.seges.acris.server.model.dto.configuration;

import sk.seges.acris.core.client.rpc.IDataTransferObject;
import sk.seges.acris.domain.shared.domain.base.ContentPkBase;
import sk.seges.sesam.pap.model.annotation.GenerateEquals;
import sk.seges.sesam.pap.model.annotation.GenerateHashcode;
import sk.seges.sesam.pap.model.annotation.Mapping;
import sk.seges.sesam.pap.model.annotation.Mapping.MappingType;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

@TransferObjectMapping(domainClass = ContentPkBase.class)
@Mapping(MappingType.AUTOMATIC)
@GenerateEquals(generate = false)
@GenerateHashcode(generate = false)
public interface ContentPkDTOConfiguration extends IDataTransferObject {
}