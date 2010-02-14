package sk.seges.corpis.dao;

import sk.seges.corpis.domain.OrderTest;
import sk.seges.sesam.dao.ICrudDAO;

public interface IOrderTestDAO extends ICrudDAO<OrderTest> {
	void persistObject(Object entity);
}
