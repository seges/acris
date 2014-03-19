package sk.seges.acris.recorder.server.dao;

import org.springframework.transaction.annotation.Transactional;
import sk.seges.acris.recorder.server.domain.AuditLog;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;
import sk.seges.sesam.dao.Page;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Transactional
public class AuditLogDAO extends AbstractHibernateCRUD<AuditLog> implements IAuditLogDAO {

	public AuditLogDAO() {
		super(AuditLog.class);
	}
	
	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager entityManager) {
		super.setEntityManager(entityManager);
	}
	
	@Override
	public AuditLog add(AuditLog log) {
		return super.persist(log);
	}

	@Override
	public List<AuditLog> load() {
		Page page = new Page();
		page.setStartIndex(0);
		page.setPageSize(100);
		return super.findAll(page).getResult();
	}
}