package sk.seges.corpis.pap.service.hibernate.service;

import sk.seges.corpis.pap.service.hibernate.model.MockEntity;
import sk.seges.sesam.pap.service.annotation.LocalServiceDefinition;

@LocalServiceDefinition(remoteService = sk.seges.corpis.pap.service.hibernate.service.TransactionalMockRemoteService.class)
public interface TransactionalMockLocalService {

	MockEntity findInTransaction();
	MockEntity findWithoutTransaction();
	MockEntity findWithoutBlob();
	MockEntity findWithoutBlobBothWays(MockEntity entity);
	MockEntity findWithReturnBlob(MockEntity entity);
}