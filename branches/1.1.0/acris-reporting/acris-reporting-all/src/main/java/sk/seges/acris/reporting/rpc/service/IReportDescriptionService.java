package sk.seges.acris.reporting.rpc.service;

import java.util.List;

import sk.seges.acris.reporting.rpc.domain.ReportDescription;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

import com.google.gwt.user.client.rpc.RemoteService;

public interface IReportDescriptionService extends RemoteService {

	ReportDescription findById(Long reportId);

	ReportDescription persist(ReportDescription report);

	void remove(Long id);

	PagedResult<List<ReportDescription>> findAllReports(Page requestedPage);
	
	Long merge(ReportDescription report);

}
