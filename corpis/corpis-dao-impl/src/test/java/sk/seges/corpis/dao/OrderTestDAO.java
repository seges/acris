package sk.seges.corpis.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;
import sk.seges.corpis.domain.OrderTest;
import sk.seges.corpis.domain.UserTest;

@Component
public class OrderTestDAO extends AbstractHibernateCRUD<OrderTest> implements IOrderTestDAO {

	protected OrderTestDAO() {
		super(OrderTest.class);
	}

	@PersistenceContext(unitName="corpisEntityManagerFactory")
    public void setEntityManager(EntityManager em) {
        super.setEntityManager(em);
    }
	
	@Override
	@Transactional
	public OrderTest findEntity(OrderTest entity) {
		return super.findEntity(entity);
	}
	
	@Override
	@Transactional
	public OrderTest persist(OrderTest entity) {
		if(entity.getUser() != null) {
			entity.setUser(entityManager.find(UserTest.class, entity.getUser().getId()));
		}
		return super.persist(entity);
	}
	
	@Transactional
	public void persistObject(Object entity) {
		entityManager.persist(entity);
	}
}
