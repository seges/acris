package sk.seges.acris.reporting.server.dao.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import sk.seges.acris.reporting.rpc.domain.ReportParameter;
import sk.seges.acris.reporting.server.dao.api.IReportParameterDao;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;

/**
 * 
 * @author psenicka
 *
 */
@Repository("reportParameterDao")
public class ReportParameterDao extends AbstractHibernateCRUD<ReportParameter> implements IReportParameterDao<ReportParameter> {

	public ReportParameterDao() {
		super(ReportParameter.class);
	}

	@Override
	public ReportParameter getEntityInstance() {
		return new ReportParameter();
	}
	
	@PersistenceContext(unitName = "acrisEntityManagerFactory")
	public void setEntityManager(EntityManager em) {
		super.setEntityManager(em);
	}
	
}
