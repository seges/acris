package sk.seges.acris.reporting.client.panel.maintainance;

import sk.seges.acris.reporting.client.panel.parameter.AbstractTypePanel;
import sk.seges.acris.reporting.rpc.domain.ReportDescription;
import sk.seges.acris.reporting.rpc.domain.ReportParameter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;

public class ReportParametersPanel extends Composite {

	FlowPanel container = GWT.create(FlowPanel.class);
	
	public ReportParametersPanel() {
		initWidget(container);
	}
	
	public void initComponents(ReportDescription report, ParameterTypeSelector parameterTypeSelector) {
		Label desc = new Label(report.getDescription());
		container.add(desc);

		FlexTable flexPanel = new FlexTable();
		flexPanel.addStyleName("ReportEditPanel-parameterPanel");
		int i = 0;
		for (ReportParameter param : report.getParametersList()) {
			Label label = new Label(param.getDisplayedName());
			AbstractTypePanel<?> widget = null;

			widget = parameterTypeSelector.getParamPanelInstance(param);

			if (widget != null) {
				widget.init(param);
	
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
				container.add(widget);
			}
		}		
	}
}
