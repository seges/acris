package sk.seges.corpis.server.service.mock;

import sk.seges.corpis.shared.model.mock.MockEntity;
import sk.seges.sesam.dao.Criterion;
import sk.seges.sesam.dao.ICrudDAO;
import sk.seges.sesam.dao.Page;

public class MockServiceGenerated {

	protected ICrudDAO<MockEntity> entityDao;
	
	public MockServiceGenerated(ICrudDAO<MockEntity> entityDao) {
		this.entityDao = entityDao;
	}

	protected Criterion getCriteriaByName(String name) {return null;};

	protected Page getPage() {
		return new Page();
	}
}
