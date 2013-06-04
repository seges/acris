package sk.seges.acris.reporting.server.dao.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.reporting.server.dao.api.IReportDescriptionDao;
import sk.seges.acris.reporting.server.domain.jpa.JpaReportDescription;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;

@Repository("reportDescriptionDao")
public class ReportDescriptionDao extends AbstractHibernateCRUD<JpaReportDescription> implements
		IReportDescriptionDao<JpaReportDescription> {

	public ReportDescriptionDao() {
		super(JpaReportDescription.class);
	}

	public JpaReportDescription findUnique(sk.seges.sesam.dao.Page page) {
		return super.findUnique(page);
	}

	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager em) {
		super.setEntityManager(em);
	}

	//
	// /* (non-Javadoc)
	// * @see
	// sk.seges.hroddelenie.dao.reporting.IReportDescription#findById(java.lang.Long)
	// */
	// public ReportDescription findById(Long id) {
	// DetachedCriteria criteria = createCriteria();
	// criteria.add(Restrictions.eq(ReportDescriptionData.ID_ATTR, id));
	// return findUniqueResultByCriteria(criteria);
	// }
	//
	// @SuppressWarnings("unchecked")
	// @Override
	// public List<ReportDescription> findByName(String name) {
	// DetachedCriteria criteria = createCriteria();
	// criteria.add(Restrictions.eq(ReportDescriptionData.NAME_ATTR, name));
	// return criteria.getExecutableCriteria((Session)
	// entityManager.getDelegate()).list();
	// }

	@Override
	public JpaReportDescription getEntityInstance() {
		return new JpaReportDescription();
	}

	@Override
	@Transactional
	public JpaReportDescription persist(JpaReportDescription entity) {
		entity.setId(null);
		return super.persist(entity);
	}

	@Override
	@Transactional
	public JpaReportDescription findEntity(JpaReportDescription entity) {
		JpaReportDescription rd = super.findEntity(entity);
		if (rd.getParametersList() != null) {
			Hibernate.initialize(rd.getParametersList());
		} else {
			rd.setParametersList(null);
		}
		return rd;
	}

}
