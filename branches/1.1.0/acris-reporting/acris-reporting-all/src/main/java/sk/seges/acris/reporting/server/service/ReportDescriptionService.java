package sk.seges.acris.reporting.server.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.sf.gilead.gwt.PersistentRemoteService;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.reporting.rpc.dao.IReportDescriptionDao;
import sk.seges.acris.reporting.rpc.domain.ReportDescription;
import sk.seges.acris.reporting.rpc.domain.ReportParameter;
import sk.seges.acris.reporting.rpc.service.IReportDescriptionService;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

@Service
public class ReportDescriptionService extends PersistentRemoteService implements IReportDescriptionService {

	private static final long serialVersionUID = -837395026110031723L;

	private static final Logger LOG = Logger.getLogger(ReportingService.class);
	
	@Autowired
	private IReportDescriptionDao reportDescriptionDao;
	
	@Override
	@Transactional
	public ReportDescription findById(Long reportId) {
		ReportDescription report = reportDescriptionDao.findById(reportId);
		Hibernate.initialize(report.getParametersList());
		List<ReportParameter> orderedParameters = report.getParametersList();
		Collections.sort(orderedParameters);
		report.setParametersList(orderedParameters);
		return report;
	}

	@Override
	@Transactional
	public ReportDescription persist(ReportDescription report) {
		report.setCreationDate(new Date());
		return reportDescriptionDao.persist(report);
	}

	@Override
	@Transactional
	public void remove(Long id) {
		ReportDescription report = reportDescriptionDao.findById(id);
		reportDescriptionDao.remove(report);
	}

	public Long merge(ReportDescription report) {
		filterRemoved(report.getParametersList());
		return reportDescriptionDao.merge(report).getId();
	}

	protected void filterRemoved(List<ReportParameter> parameters) {
		if (parameters == null)
			return;
		List<ReportParameter> newParams = new ArrayList<ReportParameter>();
		for (ReportParameter reportParameter : parameters) {
			if (!reportParameter.getClass().getName().contains("$$")) {
				newParams.add(reportParameter);
			}
		}

		parameters.clear();
		parameters.addAll(newParams);

	}

	@Transactional
	public PagedResult<List<ReportDescription>> findReports(DetachedCriteria criteria, Page requestedPage) {
		if (criteria == null)
			criteria = DetachedCriteria.forClass(ReportDescription.class);
		return reportDescriptionDao.findPagedResultByCriteria(criteria, requestedPage);
	}

	@Override
	@Transactional
	public PagedResult<List<ReportDescription>> findAllReports(Page requestedPage) {
		PagedResult<List<ReportDescription>> result = findReports(null, requestedPage);
		LOG.info("result = " + result.getTotalResultCount());
		//return result;
		return reportDescriptionDao.findAll(requestedPage);
	}

	@Override
	@Transactional
	public List<ReportDescription> findByName(String name) {
		return reportDescriptionDao.findByName(name);
	}	

}
