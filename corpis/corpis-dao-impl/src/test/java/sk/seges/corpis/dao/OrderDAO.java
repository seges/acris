package sk.seges.corpis.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;
import sk.seges.corpis.domain.Order;
import sk.seges.corpis.domain.User;

@Component
public class OrderDAO extends AbstractHibernateCRUD<Order> implements IOrderDAO {

	protected OrderDAO() {
		super(Order.class);
	}

	@PersistenceContext(unitName="corpisEntityManagerFactory")
    public void setEntityManager(EntityManager em) {
        super.setEntityManager(em);
    }
	
	@Override
	@Transactional
	public Order findEntity(Order entity) {
		return super.findEntity(entity);
	}
	
	@Override
	@Transactional
	public Order persist(Order entity) {
		if(entity.getUser() != null) {
			entity.setUser(entityManager.find(User.class, entity.getUser().getId()));
		}
		return super.persist(entity);
	}
	
	@Transactional
	public void persistObject(Object entity) {
		entityManager.persist(entity);
	}
}
