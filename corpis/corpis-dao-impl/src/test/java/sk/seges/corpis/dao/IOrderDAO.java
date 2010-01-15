package sk.seges.corpis.dao;

import sk.seges.corpis.domain.Order;
import sk.seges.sesam.dao.ICrudDAO;

public interface IOrderDAO extends ICrudDAO<Order> {
	void persistObject(Object entity);
}
