package sk.seges.acris.reporting.server.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import sk.seges.acris.reporting.server.dao.api.IReportDescriptionDao;
import sk.seges.acris.reporting.server.dao.api.IReportParameterDao;
import sk.seges.acris.reporting.shared.domain.api.ReportDescriptionData;
import sk.seges.acris.reporting.shared.domain.api.ReportParameterData;
import sk.seges.acris.reporting.shared.domain.dto.ReportDescriptionDTO;
import sk.seges.acris.reporting.shared.domain.dto.ReportParameterDTO;
import sk.seges.acris.reporting.shared.service.IReportDescriptionService;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

public class ReportDescriptionService implements IReportDescriptionService {

	private static final long serialVersionUID = -837395026110031723L;

	private static final Logger LOG = Logger
			.getLogger(ReportDescriptionService.class);

	private IReportDescriptionDao<ReportDescriptionData> reportDescriptionDao;

	private IReportParameterDao<ReportParameterData> reportParameterDao;

	public ReportDescriptionService(
			IReportDescriptionDao<ReportDescriptionData> reportDescriptionDao,
			IReportParameterDao<ReportParameterData> reportParameterDao) {
		this.reportDescriptionDao = reportDescriptionDao;
		this.reportParameterDao = reportParameterDao;
	}

	@Override
	public ReportDescriptionData findById(Long reportId) {
		ReportDescriptionData report = reportDescriptionDao.getEntityInstance();
		report.setId(reportId);
		report = reportDescriptionDao.findEntity(report);
		return convert(report);
	}

	@Override
	public ReportDescriptionData persist(ReportDescriptionData report) {
//		report.setId(System.currentTimeMillis());
		report.setCreationDate(new Date());
		if (report.getParametersList() != null
				&& !report.getParametersList().isEmpty()) {
			report.setParametersList(revertParameterListAndSetId(report
					.getParametersList()));
		}
		ReportDescriptionData result = convert(reportDescriptionDao
				.persist(revert(report)));
		return result;
	}

	@Override
	public void remove(Long id) {
		ReportDescriptionData report = findById(id);
		reportDescriptionDao.remove(revert(report));
	}

	public Long merge(ReportDescriptionData report) {
		// filterRemoved(report.getParametersList());
		// report = revert(report);
		if (report.getParametersList() != null
				&& !report.getParametersList().isEmpty()) {
			report.setParametersList(revertParameterListAndSetId(report
					.getParametersList()));
		}
		return reportDescriptionDao.merge(revert(report)).getId();
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

	@Override
	public PagedResult<List<ReportDescriptionData>> findAllReports(
			Page requestedPage) {
		PagedResult<List<ReportDescriptionData>> findAll = reportDescriptionDao
				.findAll(requestedPage);
		return convert(findAll);
	}

	@Override
	public List<ReportDescriptionData> findByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	
	// TODO replace this part by dozer or something...

	/**
	 * converts from {@link ReportDescriptionDTO} to concrete instance of
	 * {@link ReportDescriptionData} - depens on instance of
	 * {@link IReportDescriptionDao}
	 * 
	 * @param report
	 * @return entity
	 */
	public ReportDescriptionData revert(ReportDescriptionData report) {
		if (report == null) {
			return null;
		}
		ReportDescriptionData arg1 = reportDescriptionDao.getEntityInstance();
		arg1.setId(report.getId());
		arg1.setName(report.getName());
		arg1.setDescription(report.getDescription());
		arg1.setCreationDate(report.getCreationDate());
		arg1.setReportUrl(report.getReportUrl());
		arg1.setParametersList(revertParameterList(report.getParametersList()));
		return arg1;
	}

	/**
	 * converts from concrete instance of {@link ReportDescriptionData} to
	 * {@link ReportDescriptionDTO}
	 * 
	 * @param report
	 * @return DTO
	 */
	public ReportDescriptionData convert(ReportDescriptionData report) {
		if (report == null) {
			return null;
		}
		ReportDescriptionData arg1 = new ReportDescriptionDTO();
		arg1.setId(report.getId());
		arg1.setName(report.getName());
		arg1.setDescription(report.getDescription());
		arg1.setCreationDate(report.getCreationDate());
		arg1.setReportUrl(report.getReportUrl());
		arg1.setParametersList(convertParameterList(report.getParametersList()));
		return arg1;
	}

	/**
	 * converts paged result list from concrete instance of
	 * {@link ReportDescriptionData} to {@link ReportDescriptionDTO}
	 * 
	 * @param pagedResult
	 * @return
	 */
	public PagedResult<List<ReportDescriptionData>> convert(
			PagedResult<List<ReportDescriptionData>> pagedResult) {
		if (pagedResult == null || pagedResult.getResult() == null) {
			return null;
		}
		List<ReportDescriptionData> resultList = pagedResult.getResult();
		List<ReportDescriptionData> dtos = new ArrayList<ReportDescriptionData>();
		for (ReportDescriptionData reportDescriptionData : resultList) {
			ReportDescriptionData res = convert(reportDescriptionData);
			if (reportDescriptionData.getParametersList() != null
					&& !reportDescriptionData.getParametersList().isEmpty()) {
				res.setParametersList(convertParameterList(reportDescriptionData
						.getParametersList()));
			}
			dtos.add(res);
		}
		return new PagedResult<List<ReportDescriptionData>>(
				pagedResult.getPage(), dtos, pagedResult.getTotalResultCount());
	}

	/**
	 * converts concrete instance of {@link ReportParameterData} to
	 * {@link ReportParameterDTO}
	 * 
	 * @param param
	 * @return
	 */
	public ReportParameterData convert(ReportParameterData param) {
		if (param == null) {
			return null;
		}
		ReportParameterData result = new ReportParameterDTO();
		result.setId(param.getId());
		result.setName(param.getName());
		result.setClassName(param.getClassName());
		result.setDescription(param.getDescription());
		result.setDisplayedName(param.getDisplayedName());
		result.setHidden(param.getHidden());
		result.setOrderNumber(param.getOrderNumber());
		result.setParent(param.getParent());

		return result;
	}

	/**
	 * converts {@link ReportParameterDTO} to concrete instance of
	 * {@link ReportParameterData} - depends on {@link IReportParameterDao}
	 * instance
	 * 
	 * @param param
	 * @return
	 */
	public ReportParameterData revert(ReportParameterData param) {
		if (param == null) {
			return null;
		}
		ReportParameterData result = reportParameterDao.getEntityInstance();
		result.setId(param.getId());
		result.setName(param.getName());
		result.setClassName(param.getClassName());
		result.setDescription(param.getDescription());
		result.setDisplayedName(param.getDisplayedName());
		result.setHidden(param.getHidden());
		result.setOrderNumber(param.getOrderNumber());
		result.setParent(param.getParent());

		return result;
	}

	private List<ReportParameterData> revertParameterList(
			List<ReportParameterData> origParamsList) {
		if (origParamsList == null) {
			return null;
		}
		List<ReportParameterData> newParamsList = new ArrayList<ReportParameterData>();
		for (ReportParameterData param : origParamsList) {
			param = revert(param); // reportParameterDao.persist(revert(param));
			if (param != null) {
				newParamsList.add(param);
			}
		}
		return newParamsList;
	}

	private List<ReportParameterData> revertParameterListAndSetId(
			List<ReportParameterData> origParamsList) {
		if (origParamsList == null) {
			return null;
		}
		List<ReportParameterData> newParamsList = new ArrayList<ReportParameterData>();
		int i = 0;
		for (ReportParameterData param : origParamsList) {
			param = revert(param); // reportParameterDao.persist(revert(param));
			if (param.getId() == null || param.getId() < 1) {
				param.setId(System.currentTimeMillis()+(i++));
//				param = reportParameterDao.persist(revert(param));
//			} else {
//				param = reportParameterDao.merge(revert(param));
			}
			if (param != null) {
				newParamsList.add(param);
			}
		}
		return newParamsList;
	}

	private List<ReportParameterData> convertParameterList(
			List<ReportParameterData> origParamsList) {
		if (origParamsList == null) {
			return null;
		}
		List<ReportParameterData> newParamsList = new ArrayList<ReportParameterData>();
		for (ReportParameterData param : origParamsList) {
			newParamsList.add(convert(param));
		}
		return newParamsList;
	}

	// TODO replace this part by dozer

}
