package sk.seges.acris.reporting.client.panel.maintainance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sk.seges.acris.reporting.shared.domain.api.ReportDescriptionData;
import sk.seges.acris.reporting.shared.domain.api.ReportParameterData;
import sk.seges.acris.reporting.shared.domain.dto.ReportDescriptionDTO;
import sk.seges.acris.widget.client.ElementFlowPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ReportEditPanel extends Composite {

	public static final String TEXTAREA_WIDTH = "500px";
	public static final String TEXTBOX_WIDTH = "350px";
	public static final String LABEL_WIDTH = "65px";

	private ReportingMessages reportingMessages = GWT.create(ReportingMessages.class);

	private FlexTable flexTable = new FlexTable();

	protected FlowPanel container = GWT.create(FlowPanel.class);

	protected Label nameLabel = new Label(reportingMessages.name());
	protected TextBox nameText = GWT.create(TextBox.class);
	protected Label descLabel = new Label(reportingMessages.description());
	protected TextArea descText = GWT.create(TextArea.class);
	protected Label paramLabel = new Label(reportingMessages.parameters());
	protected Button addParameter;
	protected Label urlLabel = new Label(reportingMessages.url());
	protected TextBox urlText = GWT.create(TextBox.class);

//	protected VerticalPanel paramsArea = new VerticalPanel();
	protected FlowPanel paramsArea = GWT.create(FlowPanel.class);

	private Map<ParameterEditPanel, Widget> parameters = new HashMap<ParameterEditPanel, Widget>();
	private ReportDescriptionData report = new ReportDescriptionDTO();
	private long reportParameterId = 0;
	
	public ReportEditPanel() {
		initWidget(container);
	}

	@Override
	protected void onLoad() {
		initComponents();
		super.onLoad();
	}
	
	public void initComponents() {
		container.clear();
		descText.setWidth(TEXTAREA_WIDTH);

		int i = 0;
		flexTable.setWidget(i, 0, nameLabel);
		flexTable.getCellFormatter().setWidth(0, 0, LABEL_WIDTH);
		nameText.setWidth(TEXTBOX_WIDTH);
		flexTable.setWidget(i++, 1, nameText);
		flexTable.setWidget(i, 0, descLabel);
		flexTable.setWidget(i++, 1, descText);
		flexTable.setWidget(i, 0, urlLabel);
		HorizontalPanel hp = new HorizontalPanel();
		urlText.setWidth(TEXTBOX_WIDTH);
		hp.add(urlText);
		hp.add(createDownloadButtonPanel());
		flexTable.setWidget(i++, 1, hp);

		paramLabel.addStyleName("ReportEditPanel-parametersLabel");
		flexTable.setWidget(i++, 0, paramLabel);

		container.add(flexTable);
		ScrollPanel scrollPanel = GWT.create(ScrollPanel.class);
		scrollPanel.setHeight("320px");
		scrollPanel.add(paramsArea);
		
		container.add(scrollPanel);

		addParameter = new Button(reportingMessages.addParameter(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ParameterEditPanel pp = new ParameterEditPanel();
				pp.initComponents();
				pp.addStyleName("ReportEditPanel-parameterPanel");
				FlowPanel hp = new FlowPanel();
				hp.add(pp);
				hp.add(new Button("remove", removeParameter(pp)));					
				parameters.put(pp, hp);
				paramsArea.add(hp);

			}
		});

		container.add(addParameter);
	}
	
	@Override
	protected void onDetach() {
		paramsArea.clear();
		super.onDetach();
	}
	
	public void initComponents(ReportDescriptionData report) {
		initComponents();
		setActualReport(report);
	}

	private ElementFlowPanel createDownloadButtonPanel() {
		final Anchor downloadAnchor = new Anchor();
		downloadAnchor.setTarget("_new");
		downloadAnchor.setHref("#");
		final ElementFlowPanel anchorPanel = new ElementFlowPanel(downloadAnchor.getElement());
		anchorPanel.add(new Button(reportingMessages.checkUrl(),
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						if (urlText.getText() != null)
							downloadAnchor.setHref(urlText.getText());
					}
				}));
		return anchorPanel;
	}
	
	
	public ReportDescriptionData getActualReport() {
		ReportDescriptionData report = this.report;

		report.setName(nameText.getText());
		report.setDescription(descText.getText());
		report.setReportUrl(urlText.getText());

		if (parameters.size() > 0) {
			List<ReportParameterData> origParams = this.report.getParametersList();
			if (origParams != null)
				origParams.clear();
			else {
				origParams = new ArrayList<ReportParameterData>();
				report.setParametersList(origParams);
			}
			for (ParameterEditPanel parameterPanel : parameters.keySet()) {
				ReportParameterData rp = parameterPanel.getReportParameter();
				if (rp.getId() == null) {
					reportParameterId = reportParameterId + 1;
					rp.setId(reportParameterId);
				}
				origParams.add(rp);
			}
		} else {
			this.report.setParametersList(null);
		}
		
		return report;
	}
	

	public void setActualReport(ReportDescriptionData report) {
		this.report = report;
		nameText.setText(report.getName());
		descText.setText(report.getDescription());
		urlText.setText(report.getReportUrl());
		
		parameters = new HashMap<ParameterEditPanel, Widget>();
		paramsArea.clear();
		if (report.getParametersList() != null && report.getParametersList().size() > 0) {
			for (ReportParameterData param : report.getParametersList()) {
				if (param.getId() > reportParameterId) {
					reportParameterId = param.getId();
				}
				final ParameterEditPanel pp = new ParameterEditPanel();
				pp.initComponents(param);
				FlowPanel hp = new FlowPanel();
				hp.add(pp);
				hp.add(new Button("remove", removeParameter(pp)));				
				parameters.put(pp, hp);
				paramsArea.add(hp);
			}
		}
	}
	private ClickHandler removeParameter(final ParameterEditPanel pp) {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
//				paramsArea.remove(parameters.get(pp));
				paramsArea.remove(parameters.remove(pp));
			}
		};
	}	

}
