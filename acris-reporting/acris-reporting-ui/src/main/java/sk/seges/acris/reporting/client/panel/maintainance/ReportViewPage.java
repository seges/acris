package sk.seges.acris.reporting.client.panel.maintainance;

import java.util.HashMap;

import sk.seges.acris.reporting.shared.domain.api.ReportDescriptionData;
import sk.seges.acris.reporting.shared.domain.api.ReportParameterData;
import sk.seges.acris.reporting.shared.service.IReportDescriptionServiceAsync;
import sk.seges.acris.reporting.shared.service.IReportingServiceAsync;
import sk.seges.acris.widget.client.Cleaner;
import sk.seges.acris.widget.client.Dialog;
import sk.seges.acris.widget.client.action.ActionEvent;
import sk.seges.acris.widget.client.action.ActionListener;
import sk.seges.acris.widget.client.factory.StandardWidgetFactory;
import sk.seges.acris.widget.client.factory.WidgetFactory;
import sk.seges.acris.widget.client.optionpane.EMessageType;
import sk.seges.acris.widget.client.optionpane.EPanelOption;
import sk.seges.acris.widget.client.optionpane.EPanelResult;
import sk.seges.acris.widget.client.optionpane.OptionPane;
import sk.seges.acris.widget.client.optionpane.OptionPaneResultListener;
import sk.seges.acris.widget.client.optionpane.OptionsFactory;
import sk.seges.acris.widget.client.table.BeanTable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ReportViewPage extends Composite implements HasReportViewHandlers {

	private ReportingMessages reportingMessages = GWT.create(ReportingMessages.class);

	private IReportDescriptionServiceAsync reportService;// =

	// AbstractSite.factory.getChocolate(IReportDescriptionServiceAsync.REPORT_DESCRIPTION_SERVICE_NAME);

	public IReportDescriptionServiceAsync getReportService() {
		return reportService;
	}

	public void setReportService(IReportDescriptionServiceAsync reportService) {
		this.reportService = reportService;
	}

	private IReportingServiceAsync reportingService;

	private BeanTable<ReportDescriptionData> reportBeanTable;

	protected ReportDescriptionData selectedReport;

	private FlowPanel container = GWT.create(FlowPanel.class);

	private ParameterTypeSelector parameterTypeSelector;

	private String webId;
	private final WidgetFactory widgetFactory;

	public ReportViewPage() {
		widgetFactory = StandardWidgetFactory.get();
		initWidget(container);
	}

	public ReportViewPage(WidgetFactory widgetFactory) {
		this.widgetFactory = widgetFactory;
		initWidget(container);
	}

	public ReportViewPage(IReportDescriptionServiceAsync reportService, IReportingServiceAsync reportingService,
			String webId) {
		this(reportService, reportingService, webId, StandardWidgetFactory.get());
	}

	public ReportViewPage(IReportDescriptionServiceAsync reportService, IReportingServiceAsync reportingService,
			String webId, WidgetFactory widgetFactory) {
		this.reportService = reportService;
		this.reportingService = reportingService;
		this.webId = webId;
		this.widgetFactory = widgetFactory;
		initWidget(container);
	}

	public void initComponents(IReportDescriptionServiceAsync reportService, IReportingServiceAsync reportingService,
			String webId, ParameterTypeSelector parameterTypeSelector) {
		this.reportService = reportService;
		this.reportingService = reportingService;
		this.webId = webId;
		initWidget(container);
		initComponents(parameterTypeSelector);
	}

	public String getWebId() {
		return webId;
	}

	public void setWebId(String webId) {
		this.webId = webId;
	}

	public void initComponents(ParameterTypeSelector parameterTypeSelector) {
		this.parameterTypeSelector = parameterTypeSelector;
		reportBeanTable = GWT.create(IReportTableSpec.class);
		VerticalPanel content = new VerticalPanel();
		reportBeanTable.enablePager();
		reportBeanTable.setStyleName("ReportViewPage-view-table");
		reportBeanTable.addStyleName("ReportViewPage-beantable");
		reportBeanTable.setHeight("336px");
		reportBeanTable.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				reportBeanTable.getSelected(new ActionListener<ReportDescriptionData>() {
					@Override
					public void actionPerformed(ActionEvent<ReportDescriptionData> event) {
						selectedReport = event.getSource();
					}
				});
			}
		});

		content.setStyleName("ReportViewPage-contentPanel");
		content.add(createButtonPanel());
		content.add(createBeanTableWrapperPanel(reportBeanTable));

		container.add(content);

	}

	public void reload() {
		reportBeanTable.reload();
	}

	private Panel createBeanTableWrapperPanel(BeanTable<ReportDescriptionData> beanTable) {
		SimplePanel tableWrapper = new SimplePanel();
		tableWrapper.add(beanTable);

		VerticalPanel tableWrapperVP = GWT.create(VerticalPanel.class);
		tableWrapperVP.add(tableWrapper);
		SimplePanel tableWrapperHacker = WidgetFactory.hackWidget(tableWrapperVP);
		tableWrapperHacker.addStyleName("ReportViewPage-tabel-wrapper");

		return tableWrapperHacker;
	}

	private HorizontalPanel createButtonPanel() {
		HorizontalPanel buttonPanel = GWT.create(HorizontalPanel.class);
		buttonPanel.addStyleName("acris-button-panel");
		buttonPanel.addStyleName("ReportViewPage-buttonPanel");

		ClickHandler createHandler = createCreateClickHandler();
		Widget button = WidgetFactory.hackWidget(widgetFactory.button(reportingMessages.create(), createHandler));
		button.addStyleName("mainControlButton");
		buttonPanel.add(button);

		button = WidgetFactory.hackWidget(widgetFactory.button(reportingMessages.edit(), createEditClickHandler()));
		button.addStyleName("mainControlButton");
		buttonPanel.add(button);

		ClickHandler exportHandler = createExportClickHandler();
		button = WidgetFactory.hackWidget(widgetFactory.button(reportingMessages.export(), exportHandler));
		button.addStyleName("mainControlButton");
		buttonPanel.add(button);

		button = WidgetFactory.hackWidget(widgetFactory.button(reportingMessages.remove(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				selectedReport = reportBeanTable.getSelected().get(0);
				if (selectedReport != null) {
					final OptionPane pane = new OptionPane(widgetFactory).createOptionPaneFromMessage(reportingMessages.AreYouSure(),
							EMessageType.QUESTION_MESSAGE);
					final Dialog areYouSureDialog = new OptionPane(widgetFactory).createMessageDialog(reportingMessages.delete(), pane,
							false, true, EPanelOption.YES_NO_OPTION, new OptionPaneResultListener() {
								@Override
								public void onResultPrepared(EPanelResult result) {
									if (EPanelResult.YES_OPTION.equals(result)) {
										serviceRemove(pane);
									}
								}
							});
					areYouSureDialog.addStyleName("acris-reporting-remove-report");
					areYouSureDialog.show();
					areYouSureDialog.center();

				}
			}
		}));
		button.addStyleName("mainControlButton");
		buttonPanel.add(button);
		buttonPanel.add(new Cleaner());
		return buttonPanel;
	}

	private ClickHandler createEditClickHandler() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				selectedReport = reportBeanTable.getSelected().get(0);
				if (selectedReport != null) {
					serviceFindAndCreateExportDialog();
				}
			}
		};
	}

	private ClickHandler createCreateClickHandler() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				final Dialog dialog = widgetFactory.dialog();
				final ReportEditPanel rep = new ReportEditPanel();
				dialog.setCaption(reportingMessages.newReport());
				dialog.setContent(rep);
				dialog.addOptions(new OptionsFactory(widgetFactory).createOKCancelOptions(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						ReportDescriptionData report = rep.getActualReport();
						servicePersist(dialog, report);
					}
				}));
				dialog.center();
				dialog.show();
			}
		};
	}

	private ClickHandler createExportClickHandler() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				selectedReport = reportBeanTable.getSelected().get(0);
				if (selectedReport != null) {
					serviceFindById();
				}
			}
		};
	}

	protected Dialog createDownloadDialog(final ReportDescriptionData report) {
		ReportExportDialogCreator red = new ReportExportDialogCreator(widgetFactory, reportingService);
		final Dialog dialog = red.createReportExportDialog(report, new HashMap<ReportParameterData, String>(),
				parameterTypeSelector, webId);
		return dialog;
	}

	protected void serviceRemove(final OptionPane pane) {
		reportService.remove(selectedReport.getId(), new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Unable to remove report", caught);
			}

			@Override
			public void onSuccess(Void result) {
				fireEvent(new ReportViewEvent(pane, selectedReport.getId()));
			}
		});
	}

	protected void servicePersist(final Dialog dialog, ReportDescriptionData report) {
		reportService.persist(report, new AsyncCallback<ReportDescriptionData>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("cannot save report", caught);
			}

			@Override
			public void onSuccess(ReportDescriptionData result) {
				fireEvent(new ReportViewEvent(dialog, result.getId()));
			}
		});
	}

	protected void serviceFindById() {
		reportService.findById(selectedReport.getId(), new AsyncCallback<ReportDescriptionData>() {
			@Override
			public void onFailure(Throwable arg0) {
				GWT.log("Cannot find selected report = " + selectedReport.getId(), arg0);
			}

			@Override
			public void onSuccess(ReportDescriptionData arg0) {
				final Dialog dialog = createDownloadDialog(arg0);
				dialog.center();
				dialog.show();
			}
		});
	}

	protected void serviceMerge(final Dialog dialog, final ReportDescriptionData report) {
		reportService.merge(report, new AsyncCallback<Long>() {
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("cannot save report = " + report, caught);
			}

			@Override
			public void onSuccess(Long result) {
				fireEvent(new ReportViewEvent(dialog, result));
			}
		});
	}

	protected void createDialogForExport(ReportDescriptionData report) {
		final Dialog dialog = widgetFactory.dialog();
		final ReportEditPanel rep = new ReportEditPanel();
		dialog.setCaption(report.getName());
		dialog.setContent(rep);
		dialog.addOptions(new OptionsFactory(widgetFactory).createOKCancelOptions(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ReportDescriptionData report = rep.getActualReport();
				serviceMerge(dialog, report);
			}
		}));
		dialog.center();
		dialog.show();
		rep.setActualReport(report);
	}

	protected void serviceFindAndCreateExportDialog() {
		reportService.findById(selectedReport.getId(), new AsyncCallback<ReportDescriptionData>() {
			@Override
			public void onFailure(Throwable arg0) {
				GWT.log("Cannot export", arg0);
			}

			@Override
			public void onSuccess(ReportDescriptionData arg0) {
				createDialogForExport(arg0);
			}
		});
	}

	@Override
	public HandlerRegistration addReportViewHandler(ReportViewHandler handler) {
		return addHandler(handler, ReportViewEvent.TYPE);
	}

}
