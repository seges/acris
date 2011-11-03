package sk.seges.corpis.pap.service.hibernate.service;

import sk.seges.corpis.pap.service.hibernate.model.MockEntity;
import sk.seges.sesam.pap.service.annotation.LocalServiceDefinition;

@LocalServiceDefinition(remoteService = sk.seges.corpis.pap.service.hibernate.service.MockRemoteService.class)
public interface MockLocalService {

	MockEntity find();

}