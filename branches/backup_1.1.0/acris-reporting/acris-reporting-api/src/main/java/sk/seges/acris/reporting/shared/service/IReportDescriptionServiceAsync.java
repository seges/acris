package sk.seges.acris.reporting.shared.service;

import java.util.List;

import sk.seges.acris.reporting.shared.domain.api.ReportDescriptionData;
import sk.seges.acris.reporting.shared.domain.dto.ReportDescriptionDTO;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IReportDescriptionServiceAsync {
	public static final String REPORT_DESCRIPTION_SERVICE_NAME = "reportDescriptionService";
	
	void persist(ReportDescriptionData report, AsyncCallback<ReportDescriptionData> callback);

	void remove(Long id, AsyncCallback<Void> callback);

	void findAllReports(Page requestedPage, AsyncCallback<PagedResult<List<ReportDescriptionDTO>>> callback);
	
	void findById(Long reportId, AsyncCallback<ReportDescriptionData> callback);
	
	void merge(ReportDescriptionData report, AsyncCallback<Long> callback);
	
	void findByName(String name, AsyncCallback<List<ReportDescriptionData>> callback);
}
