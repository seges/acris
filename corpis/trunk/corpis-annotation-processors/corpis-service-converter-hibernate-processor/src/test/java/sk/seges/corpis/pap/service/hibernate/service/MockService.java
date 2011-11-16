package sk.seges.corpis.pap.service.hibernate.service;

import sk.seges.corpis.pap.model.entity.MockEntity;
import sk.seges.sesam.pap.service.annotation.LocalService;

@LocalService
public class MockService implements MockLocalService {

	@Override
	public MockEntity find() {
		return null;
	}
}
