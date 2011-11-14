package sk.seges.sesam.pap.service.service;

import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.pap.service.annotation.RemoteServiceDefinition;
import sk.seges.sesam.pap.service.model.MockEntity;
import sk.seges.sesam.pap.service.model.MockEntityDTO;

@RemoteServiceDefinition
@TransferObjectMapping(domainClass = MockEntity.class, dtoClass = MockEntityDTO.class)
public interface MockRemoteService {

	MockEntityDTO find();

}