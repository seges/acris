package sk.seges.acris.reporting.server.service;

import java.util.Map;

import org.apache.log4j.Logger;

import sk.seges.acris.reporting.shared.domain.api.ReportDescriptionData;
import sk.seges.acris.reporting.shared.service.IReportDescriptionService;
import sk.seges.acris.reporting.shared.service.IReportingService;

public class MockReportingService implements IReportingService {
	
	private static final Logger LOG = Logger
	.getLogger(MockReportingService.class);	
	
	IReportDescriptionService reportDescriptionService;
	
	public MockReportingService(IReportDescriptionService reportDescriptionService) {
		this.reportDescriptionService = reportDescriptionService;
	}

	@Override
	public String exportReport(Long reportDescriptionId, String exportType,
			Map<String, Object> parameters, String webId) {
		ReportDescriptionData report = reportDescriptionService.findById(reportDescriptionId);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Exporting report: " + report.getName());
		}
		return "reports/gtug4.pdf";
	}

	@Override
	public String exportReportToHtml(Long reportDescriptionId,
			Map<String, Object> parameters) {
		// TODO Auto-generated method stub
		return null;
	}

}
