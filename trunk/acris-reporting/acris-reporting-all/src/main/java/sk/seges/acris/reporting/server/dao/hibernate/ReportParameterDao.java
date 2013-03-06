package sk.seges.acris.reporting.server.dao.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import sk.seges.acris.reporting.server.dao.api.IReportParameterDao;
import sk.seges.acris.reporting.server.domain.jpa.JpaReportParameter;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;

/**
 * 
 * @author psenicka
 *
 */
@Repository("reportParameterDao")
public class ReportParameterDao extends AbstractHibernateCRUD<JpaReportParameter> implements IReportParameterDao<JpaReportParameter> {

	public ReportParameterDao() {
		super(JpaReportParameter.class);
	}

	@Override
	public JpaReportParameter getEntityInstance() {
		return new JpaReportParameter();
	}
	
	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager em) {
		super.setEntityManager(em);
	}
	
}
