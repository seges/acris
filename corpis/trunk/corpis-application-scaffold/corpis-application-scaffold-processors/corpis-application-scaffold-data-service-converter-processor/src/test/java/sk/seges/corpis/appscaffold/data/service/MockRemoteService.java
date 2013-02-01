package sk.seges.corpis.appscaffold.data.service;

import java.util.List;

import sk.seges.corpis.appscaffold.data.model.converter.MockEntityDTOConverter;
import sk.seges.corpis.appscaffold.data.model.dto.MockEntityDTO;
import sk.seges.corpis.appscaffold.data.model.entity.MockEntity;
import sk.seges.sesam.dao.PagedResult;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.pap.service.annotation.RemoteServiceDefinition;

@RemoteServiceDefinition
@TransferObjectMapping(converter = MockEntityDTOConverter.class, 
					   domainClass = MockEntity.class, dtoClass = MockEntityDTO.class)
public interface MockRemoteService {

	MockEntityDTO find();
	
	PagedResult<List<MockEntityDTO>> findAll(); 
}