package sk.seges.sesam.pap.service.service;

import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.pap.service.annotation.RemoteServiceDefinition;
import sk.seges.sesam.pap.service.model.MockEntity;
import sk.seges.sesam.pap.service.model.MockEntityDTO;

@RemoteServiceDefinition
@TransferObjectMapping(domainClass = MockEntity.class, dtoClass = MockEntityDTO.class)
public interface MockRemoteService {

	MockEntityDTO find();

	boolean primitiveMethod(boolean param1, int param2, Integer param3, String param4);
	
	Boolean objectMethod(Boolean param1, Integer param2, String param3);
}