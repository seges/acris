package sk.seges.corpis.appscaffold.data.service;

import java.util.List;

import sk.seges.corpis.appscaffold.data.model.entity.MockEntity;
import sk.seges.sesam.dao.PagedResult;
import sk.seges.sesam.pap.service.annotation.LocalServiceDefinition;

@LocalServiceDefinition(remoteService = sk.seges.corpis.appscaffold.data.service.MockRemoteService.class)
public interface MockLocalService {

	MockEntity find();
	
	PagedResult<List<MockEntity>> findAll(); 
}