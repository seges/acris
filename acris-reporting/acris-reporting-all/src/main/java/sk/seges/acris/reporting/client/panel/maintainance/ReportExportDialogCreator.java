package sk.seges.acris.reporting.client.panel.maintainance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sk.seges.acris.core.client.util.JavascriptUtils;
import sk.seges.acris.reporting.client.panel.parameter.AbstractTypePanel;
import sk.seges.acris.reporting.client.panel.parameter.IParameterTypePanel;
import sk.seges.acris.reporting.rpc.domain.EReportExportType;
import sk.seges.acris.reporting.rpc.domain.ReportDescription;
import sk.seges.acris.reporting.rpc.domain.ReportParameter;
import sk.seges.acris.reporting.rpc.service.IReportingServiceAsync;
import sk.seges.acris.widget.client.Dialog;
import sk.seges.acris.widget.client.WidgetFactory;
import sk.seges.acris.widget.client.advanced.EnumListBoxWithValue;
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
	private Map<ReportParameter, String> predefinedParams;

	public ReportExportDialogCreator() {}

	public ReportExportDialogCreator(IReportingServiceAsync reportingService) {
		this.reportingService = reportingService;
	}

	public Dialog createReportExportDialog(ReportDescription report, Map<ReportParameter, String> predefinedParams,
			ParameterTypeSelector parameterTypeSelector, String webId) {
		this.webId = webId;
		this.predefinedParams = predefinedParams;
		this.init(report, predefinedParams, parameterTypeSelector);
		return dialog;
	}

	private Panel downloadPanel;
	
	void init(ReportDescription report, Map<ReportParameter, String> predefinedParams,
			ParameterTypeSelector parameterTypeSelector) {
		dialog = WidgetFactory.dialog();
		dialog.setModal(false);
		paramWidgets = new ArrayList<IParameterTypePanel<?>>();
		dialog.setCaption(report.getName());
		dialog.addStyleName("employees-import-dialog");

		FlowPanel contentPanel = createDownloadDialogContent(report, predefinedParams, parameterTypeSelector);
		ScrollPanel scrollPanel = GWT.create(ScrollPanel.class);
		scrollPanel.setWidth("700px");
//		scrollPanel.setHeight("500px");
		scrollPanel.add(contentPanel);
		dialog.setContent(scrollPanel);
		dialog.addOption(OptionsFactory.createCloseOption());
		downloadPanel = createDownloadButtonPanel();
		downloadPanel.setVisible(false);
		dialog.addOptionWithoutHiding(createExportButton(report, downloadPanel));
		dialog.addOption(WidgetFactory.hackWidget(downloadPanel));
		imagePanel = new SimplePanel();
		imagePanel.add(loadingImage);
		imagePanel.setVisible(false);
		dialog.addOption(WidgetFactory.hackWidget(imagePanel));
	}

	private Button createExportButton(final ReportDescription report, final Panel downloadPanel) {
		return WidgetFactory.button(reportingMessages.export(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				imagePanel.setVisible(true);
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
					for (ReportParameter predefParam : predefinedParams.keySet()) {
						paramsMap.put(predefParam.getName(), predefinedParams.get(predefParam));
					}
				}
				if (notInsertedValues.size() > 0) {
					imagePanel.setVisible(false);
					StringBuffer errorMsg = new StringBuffer();
					for (String string : notInsertedValues) {
						errorMsg.append("<br />"+string);
					}
					Dialog errorDialog = OptionPane.createErrorDialog(
							"<b>Treba zadat tieto parametre:</b>" + errorMsg, "Pozor");
					errorDialog.setWidth("400px");
					errorDialog.center();
					errorDialog.show();
				} else
					doExport(report, downloadPanel, paramsMap);
			}
		});
	}

	private FlowPanel createDownloadDialogContent(final ReportDescription report,
			Map<ReportParameter, String> predefinedParams, ParameterTypeSelector parameterTypeSelector) {
		FlowPanel contentPanel = GWT.create(FlowPanel.class);
		Label desc = new Label(report.getDescription());
		contentPanel.add(desc);

		FlexTable flexPanel = new FlexTable();
		flexPanel.addStyleName("ReportEditPanel-parameterPanel");
		int i = 0;
		List<AbstractTypePanel<?>> predefinedNotVisibleWidgets = new ArrayList<AbstractTypePanel<?>>();
		for (ReportParameter param : report.getParametersList()) {
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

		Label label = new Label(reportingMessages.exportFileType());
		exportTypeListBox = GWT.create(EnumListBoxWithValue.class);
		exportTypeListBox.setClazz(EReportExportType.class);
		exportTypeListBox.load(Arrays.asList(EReportExportType.values()));
		exportTypeListBox.removeItem(0);
		exportTypeListBox.setSelectedIndex(0);
		flexPanel.setWidget(++i, 0, label);
		flexPanel.setWidget(i, 1, exportTypeListBox);

		contentPanel.add(flexPanel);
		return contentPanel;
	}

	private Panel createDownloadButtonPanel() {
	    /*
		final Anchor downloadAnchor = new Anchor();
		downloadAnchor.setTarget("_new");
		downloadAnchor.setHref("#");
		final ElementFlowPanel anchorPanel = new ElementFlowPanel(downloadAnchor.getElement());
		/*/
	    FlowPanel anchorPanel = new FlowPanel();
	    /**/
		anchorPanel.add(WidgetFactory.hackWidget(WidgetFactory.button(reportingMessages.download(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
//				downloadAnchor.setHref(exportPath);
				JavascriptUtils.getURL(exportPath);
			}
		})));
		return anchorPanel;
	}

	/**/
	protected void doExport(final ReportDescription report, final Panel downloadPanel, Map<String, Object> paramsMap) {
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
		{
			reportingService.exportReport(report.getId(), exportTypeListBox.getValue().getName(), paramsMap, webId,
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							imagePanel.setVisible(false);
							GWT.log("export error",
									new Exception("Export sa nevydaril"));
							OptionPane.createErrorDialog(reportingMessages.exportError(), reportingMessages.export());
						}

						@Override
						public void onSuccess(String result) {
							imagePanel.setVisible(false);
							if (result != null) {
								exportPath = result;
								downloadPanel.setVisible(true);
//								JavascriptUtils.getURL(exportPath);
							} else {
								OptionPane.showErrorDialog("Vyskytla sa chyba pri generovani reportu.", "export error");
							}
						}
					});
		}
	}

	protected void createHtmlExportDialog(String result) {
		Dialog d = WidgetFactory.dialog();
		HTML html = new HTML(result);
		ScrollPanel sp = new ScrollPanel();
		sp.setHeight("500px");
		sp.add(html);
		d.add(sp);
		d.addOption(OptionsFactory.createCloseOption());
		d.center();
		d.show();
	}

}
