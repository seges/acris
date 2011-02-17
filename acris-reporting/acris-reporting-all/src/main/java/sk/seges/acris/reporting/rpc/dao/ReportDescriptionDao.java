package sk.seges.acris.reporting.rpc.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import sk.seges.acris.reporting.rpc.domain.ReportDescription;
import sk.seges.acris.reporting.server.dao.api.IReportDescriptionDao;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;

@Component
public class ReportDescriptionDao extends AbstractHibernateCRUD<ReportDescription> implements IReportDescriptionDao<ReportDescription> {

	protected ReportDescriptionDao() {
		super(ReportDescription.class);
	}
	
	public ReportDescription findUnique(sk.seges.sesam.dao.Page page) {
		return super.findUnique(page);
	}
	
	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager em) {
		super.setEntityManager(em);
	}
//	
//	/* (non-Javadoc)
//	 * @see sk.seges.hroddelenie.dao.reporting.IReportDescription#findById(java.lang.Long)
//	 */
//	public ReportDescription findById(Long id) {
//		DetachedCriteria criteria = createCriteria();
//		criteria.add(Restrictions.eq(ReportDescriptionData.ID_ATTR, id));
//		return findUniqueResultByCriteria(criteria);
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<ReportDescription> findByName(String name) {
//		DetachedCriteria criteria = createCriteria();
//		criteria.add(Restrictions.eq(ReportDescriptionData.NAME_ATTR, name));
//		return criteria.getExecutableCriteria((Session) entityManager.getDelegate()).list();
//	}

	@Override
	public ReportDescription getEntityInstance() {
		return new ReportDescription();
	}

}
