package sk.seges.acris.reporting.client.samples;

import sk.seges.acris.reporting.client.panel.maintainance.ParameterTypeSelector;
import sk.seges.acris.reporting.client.panel.maintainance.ReportViewEvent;
import sk.seges.acris.reporting.client.panel.maintainance.ReportViewHandler;
import sk.seges.acris.reporting.client.panel.maintainance.ReportViewPage;
import sk.seges.acris.reporting.shared.service.IReportDescriptionService;
import sk.seges.acris.reporting.shared.service.IReportDescriptionServiceAsync;
import sk.seges.acris.reporting.shared.service.IReportingService;
import sk.seges.acris.reporting.shared.service.IReportingServiceAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class SimpleReporting implements EntryPoint {

	@Override
	public void onModuleLoad() {
		final IReportDescriptionServiceAsync reportService = GWT
				.create(IReportDescriptionService.class);
		IReportingServiceAsync reportingService = GWT
				.create(IReportingService.class);
		ServiceDefTarget reportServiceDefTarget = (ServiceDefTarget) reportService;
		reportServiceDefTarget
				.setServiceEntryPoint(GWT.getModuleBaseURL()+"showcase-service/reportDescriptionService");
		ServiceDefTarget reportingServiceDefTarget = (ServiceDefTarget) reportingService;
		reportingServiceDefTarget.setServiceEntryPoint(GWT.getModuleBaseURL()+"showcase-service/reportingService");

		final ReportViewPage reportViewPage = new ReportViewPage(reportService,
				reportingService, "showcase");
		reportViewPage.initComponents(new ParameterTypeSelector());
		reportViewPage.addReportViewHandler(new ReportViewHandler() {
			
			@Override
			public void onChange(ReportViewEvent event) {
				reportViewPage.reload();
			}
		});
		FlowPanel panel = GWT.create(FlowPanel.class);
		panel.addStyleName("acris-reporting-showcase-content");
		panel.add(reportViewPage);
		RootPanel.get().add(panel);
	}
}
