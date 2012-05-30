package sk.seges.acris.reporting.client.panel.maintainance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sk.seges.acris.core.client.util.JavaScriptUtils;
import sk.seges.acris.reporting.client.panel.parameter.AbstractTypePanel;
import sk.seges.acris.reporting.client.panel.parameter.IParameterTypePanel;
import sk.seges.acris.reporting.shared.domain.api.EReportExportType;
import sk.seges.acris.reporting.shared.domain.api.ReportDescriptionData;
import sk.seges.acris.reporting.shared.domain.api.ReportParameterData;
import sk.seges.acris.reporting.shared.service.IReportingServiceAsync;
import sk.seges.acris.widget.client.Dialog;
import sk.seges.acris.widget.client.advanced.EnumListBoxWithValue;
import sk.seges.acris.widget.client.factory.WidgetFactory;
import sk.seges.acris.widget.client.optionpane.OptionPane;
import sk.seges.acris.widget.client.optionpane.OptionsFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;

public class ReportExportDialogCreator {

	protected EnumListBoxWithValue<EReportExportType> exportTypeListBox;
	private List<IParameterTypePanel<?>> paramWidgets;
	protected ReportingMessages reportingMessages = GWT.create(ReportingMessages.class);
	private IReportingServiceAsync reportingService;// =
	// AbstractSite.factory.getChocolate("reportingService");
	protected String exportPath = null;
	private Dialog dialog;
	private Image loadingImage = new Image("scrollTableLoading.gif");
	protected SimplePanel imagePanel;
	protected String webId;
	private Map<ReportParameterData, String> predefinedParams;

	private WidgetFactory widgetFactory;

	protected Dialog getDialog() {
		return dialog;
	}

	public ReportExportDialogCreator(WidgetFactory widgetFactory) {
		this.widgetFactory = widgetFactory;
	}

	public ReportExportDialogCreator(WidgetFactory widgetFactory, IReportingServiceAsync reportingService) {
		this(widgetFactory);
		this.reportingService = reportingService;
	}
	
	public Dialog createReportExportDialog(ReportDescriptionData report,
			Map<ReportParameterData, String> predefinedParams, ParameterTypeSelector parameterTypeSelector, String webId, String reportName,
			 String locale) {
		this.webId = webId;
		this.predefinedParams = predefinedParams;
		this.init(report, predefinedParams, parameterTypeSelector, reportName, locale);
		return dialog;
	}
	

	public Dialog createReportExportDialog(ReportDescriptionData report,
			Map<ReportParameterData, String> predefinedParams, ParameterTypeSelector parameterTypeSelector, String webId) {
		return createReportExportDialog(report, predefinedParams, parameterTypeSelector, webId, null);
	}

	public Dialog createReportExportDialog(ReportDescriptionData report,
			Map<ReportParameterData, String> predefinedParams, ParameterTypeSelector parameterTypeSelector,
			String webId, String reportName) {
		return createReportExportDialog(report, predefinedParams, parameterTypeSelector, webId, reportName, null);
	}

	private Panel downloadPanel;
	protected ScrollPanel scrollPanel;

	protected void setDialogSize() {
		scrollPanel.setWidth("700px");
		scrollPanel.setHeight("500px");
	}

	void init(ReportDescriptionData report, Map<ReportParameterData, String> predefinedParams,
			ParameterTypeSelector parameterTypeSelector, String reportName, String locale) {
		dialog = widgetFactory.dialog();
		dialog.setModal(false);
		paramWidgets = new ArrayList<IParameterTypePanel<?>>();
		dialog.setCaption(reportName != null ? reportName : (report.getDisplayName() == null) ? report.getName()
				: report.getDisplayName());
		dialog.addStyleName("report-export-dialog");

		FlowPanel contentPanel = createDownloadDialogContent(report, predefinedParams, parameterTypeSelector);
		scrollPanel = GWT.create(ScrollPanel.class);
		scrollPanel.add(contentPanel);
		setDialogSize();
		dialog.setContent(scrollPanel);
		dialog.addOption(new OptionsFactory(widgetFactory).createCloseOption());
		downloadPanel = createDownloadButtonPanel();
		downloadPanel.setVisible(false);
		dialog.addOptionWithoutHiding(createExportButton(report, downloadPanel, locale));
		dialog.addOption(WidgetFactory.hackWidget(downloadPanel));
		imagePanel = new SimplePanel();
		imagePanel.add(loadingImage);
		imagePanel.setVisible(false);
		dialog.addOption(WidgetFactory.hackWidget(imagePanel));
	}

	private Button createExportButton(final ReportDescriptionData report, final Panel downloadPanel, String locale) {
		return widgetFactory.button(reportingMessages.export(), new ExportClickHandler(report, downloadPanel, locale));
	}

	private class ExportClickHandler implements ClickHandler {

		private ReportDescriptionData report;
		private Panel downloadPanel;
		private String locale;

		/**
		 * @param report
		 * @param downloadPanel
		 * @param locale 
		 */
		public ExportClickHandler(ReportDescriptionData report, Panel downloadPanel, String locale) {
			this.report = report;
			this.downloadPanel = downloadPanel;
			this.locale = locale;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt
		 * .event.dom.client.ClickEvent)
		 */
		@Override
		public void onClick(ClickEvent event) {
			downloadPanel.setVisible(false);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			List<String> notInsertedValues = new ArrayList<String>();
			for (IParameterTypePanel<?> w : paramWidgets) {
				Object value = w.getValue();
				if (value == null && !predefinedParams.containsKey(w.getReportParameter())) {
					notInsertedValues.add(w.getReportParameter().getDisplayedName());
				}
				String s = (value == null) ? "" : value.toString();
				paramsMap.put(w.getReportParameter().getName(), s);
			}
			if (predefinedParams.size() > 0) {
				for (ReportParameterData predefParam : predefinedParams.keySet()) {
					paramsMap.put(predefParam.getName(), predefinedParams.get(predefParam));
					paramsMap.put(predefParam.getName(), predefinedParams.get(predefParam));
				}
			}
			
			if (notInsertedValues.size() > 0) {
				imagePanel.setVisible(false);
				StringBuffer errorMsg = new StringBuffer();
				for (String string : notInsertedValues) {
					errorMsg.append("<br />" + string);
				}
				Dialog errorDialog = new OptionPane(widgetFactory).createErrorDialog(
						"<b>Treba zadat tieto parametre:</b>" + errorMsg, "Pozor");
				errorDialog.setWidth("400px");
				errorDialog.center();
				errorDialog.show();
			} else {
				doExport(report, downloadPanel, paramsMap);
			}

		}

	}

	private FlowPanel createDownloadDialogContent(final ReportDescriptionData report,
			Map<ReportParameterData, String> predefinedParams, ParameterTypeSelector parameterTypeSelector) {
		FlowPanel contentPanel = GWT.create(FlowPanel.class);
		Label desc = new Label(report.getDescription());
		contentPanel.add(desc);

		FlexTable flexPanel = new FlexTable();
		flexPanel.addStyleName("ReportEditPanel-parameterPanel");
		int i = 0;
		if (report.getParametersList() != null) {
			List<AbstractTypePanel<?>> predefinedNotVisibleWidgets = new ArrayList<AbstractTypePanel<?>>();
			for (ReportParameterData param : report.getParametersList()) {
				Label label = new Label(param.getDisplayedName());
				AbstractTypePanel<?> widget = null;

				widget = parameterTypeSelector.getParamPanelInstance(param);
				widget.init(param);
				if (predefinedParams == null || !predefinedParams.containsKey(param)) {
					Label wDesc = new Label(param.getDescription());
					wDesc.addStyleName("ReportExportPanel-parameterPanel");
					if (!Boolean.TRUE.equals(param.getHidden())) {
						i++;
						label.addStyleName("ReportExportPanel-parameterPanel");
						flexPanel.setWidget(i, 0, label);
						flexPanel.getFlexCellFormatter().setVerticalAlignment(i, 0, HasVerticalAlignment.ALIGN_TOP);
						flexPanel.setWidget(i, 1, wDesc);
						// flexPanel.getFlexCellFormatter().setColSpan(i, 0, 2);
						i++;
						flexPanel.setWidget(i, 1, widget);
					}
				} else {
					predefinedNotVisibleWidgets.add(widget);
					widget.setValue(predefinedParams.get(param));
				}
				paramWidgets.add(widget);
			}

			for (AbstractTypePanel<?> widget : predefinedNotVisibleWidgets) {
				widget.setValue(predefinedParams.get(widget.getReportParameter()));
			}
		}
		// Label label = new Label(reportingMessages.exportFileType());
		// exportTypeListBox = GWT.create(EnumListBoxWithValue.class);
		// exportTypeListBox.setClazz(EReportExportType.class);
		// exportTypeListBox.load(Arrays.asList(EReportExportType.values()));
		// exportTypeListBox.removeItem(0);
		// exportTypeListBox.setSelectedIndex(0);
		// flexPanel.setWidget(++i, 0, label);
		// flexPanel.setWidget(i, 1, exportTypeListBox);

		contentPanel.add(flexPanel);
		return contentPanel;
	}

	private Panel createDownloadButtonPanel() {
		/*
		 * final Anchor downloadAnchor = new Anchor();
		 * downloadAnchor.setTarget("_new"); downloadAnchor.setHref("#"); final
		 * ElementFlowPanel anchorPanel = new
		 * ElementFlowPanel(downloadAnchor.getElement()); /
		 */
		FlowPanel anchorPanel = new FlowPanel();
		/**/
		anchorPanel.add(WidgetFactory.hackWidget(widgetFactory.button(reportingMessages.download(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				// downloadAnchor.setHref(exportPath);
				JavaScriptUtils.getURL(exportPath);
			}
		})));
		return anchorPanel;
	}

	/**/
	protected void doExport(final ReportDescriptionData report, final Panel downloadPanel, Map<String, Object> paramsMap) {
		imagePanel.setVisible(true);
		// if (EReportExportType.HTML.equals(exportTypeListBox.getValue())) {
		// reportingService.exportReportToHtml(report.getId(), paramsMap, new
		// AsyncCallback<String>() {
		// @Override
		// public void onSuccess(String result) {
		// imagePanel.setVisible(false);
		// createHtmlExportDialog(result);
		// }
		//
		// @Override
		// public void onFailure(Throwable arg0) {
		// imagePanel.setVisible(false);
		// GWT.log("export error", arg0);
		// }
		// });
		//
		// } else
		// {
		reportingService.exportReport(report.getId(), null, paramsMap, webId, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				imagePanel.setVisible(false);
				GWT.log("export error", new Exception("Export sa nevydaril"));
				new OptionPane(widgetFactory).createErrorDialog(reportingMessages.exportError(),
						reportingMessages.export());
			}

			@Override
			public void onSuccess(String result) {
				imagePanel.setVisible(false);
				if (result != null) {
					exportPath = result;
					downloadPanel.setVisible(true);
					// JavascriptUtils.getURL(exportPath);
				} else {
					new OptionPane(widgetFactory).showErrorDialog("Vyskytla sa chyba pri generovani reportu.",
							"export error");
				}
			}
		});
		// }
	}

	protected void createHtmlExportDialog(String result) {
		Dialog d = widgetFactory.dialog();
		HTML html = new HTML(result);
		ScrollPanel sp = new ScrollPanel();
		sp.setHeight("500px");
		sp.add(html);
		d.add(sp);
		d.addOption(new OptionsFactory(widgetFactory).createCloseOption());
		d.center();
		d.show();
	}

}
