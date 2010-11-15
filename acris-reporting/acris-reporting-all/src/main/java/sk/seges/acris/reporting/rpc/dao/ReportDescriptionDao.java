package sk.seges.acris.reporting.rpc.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import sk.seges.acris.reporting.rpc.domain.ReportDescription;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;

@Component
public class ReportDescriptionDao extends AbstractHibernateCRUD<ReportDescription> implements IReportDescriptionDao {

	protected ReportDescriptionDao() {
		super(ReportDescription.class);
	}
	
	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager em) {
		super.setEntityManager(em);
	}
	
	/* (non-Javadoc)
	 * @see sk.seges.hroddelenie.dao.reporting.IReportDescription#findById(java.lang.Long)
	 */
	public ReportDescription findById(Long id) {
		DetachedCriteria criteria = createCriteria();
		criteria.add(Restrictions.eq(ReportDescription.ID_ATTR, id));
		return findUniqueResultByCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ReportDescription> findByName(String name) {
		DetachedCriteria criteria = createCriteria();
		criteria.add(Restrictions.eq(ReportDescription.NAME_ATTR, name));
		return criteria.getExecutableCriteria((Session) entityManager.getDelegate()).list();
	}

}
