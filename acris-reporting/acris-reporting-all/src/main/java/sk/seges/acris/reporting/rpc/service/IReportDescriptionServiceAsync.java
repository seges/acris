package sk.seges.acris.reporting.rpc.service;

import java.util.List;

import sk.seges.acris.reporting.rpc.domain.ReportDescription;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IReportDescriptionServiceAsync {
	public static final String FIND_ALL_REPORTS_METHOD="findAllReports";
	public static final String REPORT_DESCRIPTION_SERVICE_NAME = "reportDescriptionService";
	
	void persist(ReportDescription report, AsyncCallback<ReportDescription> callback);

	void remove(Long id, AsyncCallback<Void> callback);

	void findAllReports(Page requestedPage, AsyncCallback<PagedResult<List<ReportDescription>>> callback);
	
	void findById(Long reportId, AsyncCallback<ReportDescription> callback);
	
	void merge(ReportDescription report, AsyncCallback<Long> callback);
}
