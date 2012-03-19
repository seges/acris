package sk.seges.corpis.appscaffold.data.service;

import java.util.List;

import sk.seges.corpis.appscaffold.data.model.entity.MockEntity;
import sk.seges.sesam.dao.PagedResult;
import sk.seges.sesam.pap.service.annotation.LocalService;

@LocalService
public class MockService implements MockLocalService {

	@Override
	public MockEntity find() {
		return null;
	}

	@Override
	public PagedResult<List<MockEntity>> findAll() {
		return null;
	}
}
