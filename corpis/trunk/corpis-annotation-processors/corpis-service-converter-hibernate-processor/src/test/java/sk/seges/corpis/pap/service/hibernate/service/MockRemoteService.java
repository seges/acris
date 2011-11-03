package sk.seges.corpis.pap.service.hibernate.service;

import sk.seges.corpis.pap.service.hibernate.model.MockEntity;
import sk.seges.corpis.pap.service.hibernate.model.MockEntityConverter;
import sk.seges.corpis.pap.service.hibernate.model.MockEntityDTO;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.pap.service.annotation.RemoteServiceDefinition;

@RemoteServiceDefinition
@TransferObjectMapping(converter = MockEntityConverter.class, 
					   domainClass = MockEntity.class, dtoClass = MockEntityDTO.class)
public interface MockRemoteService {

	MockEntityDTO find();

}