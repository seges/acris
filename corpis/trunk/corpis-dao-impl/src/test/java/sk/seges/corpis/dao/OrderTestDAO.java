package sk.seges.corpis.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;
import sk.seges.corpis.domain.OrderTestDO;
import sk.seges.corpis.domain.UserTestDO;

@Component
public class OrderTestDAO extends AbstractHibernateCRUD<OrderTestDO> implements IOrderTestDAO {

	protected OrderTestDAO() {
		super(OrderTestDO.class);
	}

	@PersistenceContext(unitName="corpisEntityManagerFactory")
    public void setEntityManager(EntityManager em) {
        super.setEntityManager(em);
    }
	
	@Override
	@Transactional
	public OrderTestDO findEntity(OrderTestDO entity) {
		return super.findEntity(entity);
	}
	
	@Override
	@Transactional
	public OrderTestDO persist(OrderTestDO entity) {
		if(entity.getUser() != null) {
			entity.setUser(entityManager.find(UserTestDO.class, entity.getUser().getId()));
		}
		return super.persist(entity);
	}
	
	@Transactional
	public void persistObject(Object entity) {
		entityManager.persist(entity);
	}
}
