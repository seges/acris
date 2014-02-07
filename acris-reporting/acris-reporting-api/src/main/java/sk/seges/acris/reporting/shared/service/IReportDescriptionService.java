package sk.seges.acris.reporting.shared.service;

import com.google.gwt.user.client.rpc.RemoteService;
import sk.seges.acris.reporting.shared.domain.api.ReportDescriptionData;
import sk.seges.sesam.shared.model.dto.PageDTO;
import sk.seges.sesam.shared.model.dto.PagedResultDTO;
import sk.seges.sesam.pap.service.annotation.RemoteServiceDefinition;

import java.util.List;

@RemoteServiceDefinition
public interface IReportDescriptionService extends RemoteService {

	public static final String FIND_ALL_REPORTS_METHOD = "findAllReports";

	ReportDescriptionData findById(Long reportId);
	
	List<ReportDescriptionData> findByName(String name);

	ReportDescriptionData persist(ReportDescriptionData report);

	void remove(Long id);

	PagedResultDTO<List<ReportDescriptionData>> findAllReports(PageDTO requestedPage);
	
	Long merge(ReportDescriptionData report);

}
