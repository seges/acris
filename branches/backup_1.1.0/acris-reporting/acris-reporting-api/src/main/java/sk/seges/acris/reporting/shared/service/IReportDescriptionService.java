package sk.seges.acris.reporting.shared.service;

import java.util.List;

import sk.seges.acris.reporting.shared.domain.api.ReportDescriptionData;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

import com.google.gwt.user.client.rpc.RemoteService;

public interface IReportDescriptionService extends RemoteService {
	public static final String FIND_ALL_REPORTS_METHOD="findAllReports";

	ReportDescriptionData findById(Long reportId);
	
	List<ReportDescriptionData> findByName(String name);

	ReportDescriptionData persist(ReportDescriptionData report);

	void remove(Long id);

	PagedResult<List<ReportDescriptionData>> findAllReports(Page requestedPage);
	
	Long merge(ReportDescriptionData report);

}
