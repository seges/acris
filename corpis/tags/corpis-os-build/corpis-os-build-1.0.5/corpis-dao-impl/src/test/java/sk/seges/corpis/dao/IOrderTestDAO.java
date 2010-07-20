package sk.seges.corpis.dao;

import sk.seges.corpis.domain.OrderTestDO;
import sk.seges.sesam.dao.ICrudDAO;

public interface IOrderTestDAO extends ICrudDAO<OrderTestDO> {
	void persistObject(Object entity);
}
