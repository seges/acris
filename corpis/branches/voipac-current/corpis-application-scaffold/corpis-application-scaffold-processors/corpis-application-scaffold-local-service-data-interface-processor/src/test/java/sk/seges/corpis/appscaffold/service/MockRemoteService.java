package sk.seges.corpis.appscaffold.service;

import java.util.List;

import sk.seges.corpis.appscaffold.model.MockDto;
import sk.seges.sesam.pap.service.annotation.RemoteServiceDefinition;

@RemoteServiceDefinition
public interface MockRemoteService {
	
	MockDto getMockDto();
	
	List<MockDto> findAllMocks();
}	

