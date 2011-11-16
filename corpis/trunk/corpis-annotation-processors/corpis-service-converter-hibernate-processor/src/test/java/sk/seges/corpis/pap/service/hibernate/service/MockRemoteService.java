package sk.seges.corpis.pap.service.hibernate.service;

import sk.seges.corpis.pap.model.converter.MockEntityDTOConverter;
import sk.seges.corpis.pap.model.dto.MockEntityDTO;
import sk.seges.corpis.pap.model.entity.MockEntity;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.pap.service.annotation.RemoteServiceDefinition;

@RemoteServiceDefinition
@TransferObjectMapping(converter = MockEntityDTOConverter.class, 
					   domainClass = MockEntity.class, dtoClass = MockEntityDTO.class)
public interface MockRemoteService {

	MockEntityDTO find();

}