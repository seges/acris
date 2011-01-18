package sk.seges.acris.reporting.server.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.reporting.rpc.dao.IReportDescriptionDao;
import sk.seges.acris.reporting.shared.domain.api.ReportDescriptionData;
import sk.seges.acris.reporting.shared.domain.api.ReportParameterData;
import sk.seges.acris.reporting.shared.service.IReportDescriptionService;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@Service
public class ReportDescriptionService extends RemoteServiceServlet implements IReportDescriptionService {

	private static final long serialVersionUID = -837395026110031723L;

	private static final Logger LOG = Logger.getLogger(ReportDescriptionService.class);
	
	@Autowired
	private IReportDescriptionDao<ReportDescriptionData> reportDescriptionDao;
	
	@Override
	@Transactional
	public ReportDescriptionData findById(Long reportId) {
//		ReportDescriptionData report = reportDescriptionDao.findById(reportId);
//		Hibernate.initialize(report.getParametersList());
//		return report;
		return null;
	}

	@Override
	@Transactional
	public ReportDescriptionData persist(ReportDescriptionData report) {
		report.setCreationDate(new Date());
		return null;
//		return reportDescriptionDao.persist(report);
	}

	@Override
	@Transactional
	public void remove(Long id) {
//		ReportDescriptionData report = reportDescriptionDao.findById(id);
//		reportDescriptionDao.remove(report);
	}

	public Long merge(ReportDescriptionData report) {
		filterRemoved(report.getParametersList());
		return null;
//		return reportDescriptionDao.merge(report).getId();
	}

	protected void filterRemoved(List<ReportParameterData> parameters) {
		if (parameters == null)
			return;
		List<ReportParameterData> newParams = new ArrayList<ReportParameterData>();
		for (ReportParameterData reportParameter : parameters) {
			if (!reportParameter.getClass().getName().contains("$$")) {
				newParams.add(reportParameter);
			}
		}

		parameters.clear();
		parameters.addAll(newParams);

	}

	@Transactional
	public PagedResult<List<ReportDescriptionData>> findReports(DetachedCriteria criteria, Page requestedPage) {
//		if (criteria == null) {
//			criteria = DetachedCriteria.forClass(ReportDescriptionData.class);
//		}
//		return reportDescriptionDao.findPagedResultByCriteria(criteria, requestedPage);
		return null;
	}

	@Override
	@Transactional
	public PagedResult<List<ReportDescriptionData>> findAllReports(Page requestedPage) {
		PagedResult<List<ReportDescriptionData>> result = findReports(null, requestedPage);
		LOG.info("result = " + result.getTotalResultCount());
		//return result;
		return reportDescriptionDao.findAll(requestedPage);
	}

	@Override
	@Transactional
	public List<ReportDescriptionData> findByName(String name) {
//		return reportDescriptionDao.findByName(name);
		return null;
	}	

}
