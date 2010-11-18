package sk.seges.acris.reporting.client.panel.maintainance;

import sk.seges.acris.reporting.client.panel.parameter.AbstractTypePanel;
import sk.seges.acris.reporting.client.panel.parameter.CheckBoxTypePanel;
import sk.seges.acris.reporting.client.panel.parameter.DateTypePanel;
import sk.seges.acris.reporting.client.panel.parameter.StringTypePanel;
import sk.seges.acris.reporting.rpc.domain.ReportParameter;
import sk.seges.acris.reporting.shared.domain.api.EReportParameterType;

import com.google.gwt.core.client.GWT;

/**
 * 
 * @author marta
 * 
 * @param <E>
 */
public class ParameterTypeSelector {

	protected AbstractTypePanel<?> getParamPanelInstance(ReportParameter param) {
		AbstractTypePanel<?> widget = null;

		switch (Enum.valueOf(EReportParameterType.class, param.getClassName())) {
		case DATE:
			widget = GWT.create(DateTypePanel.class);
			break;
		case NUMBER:
			widget = GWT.create(StringTypePanel.class);
			break;
		case STRING:
			widget = GWT.create(StringTypePanel.class);
			break;
		case BOOLEAN:
			widget = GWT.create(CheckBoxTypePanel.class);
			break;
		default:
			widget = GWT.create(StringTypePanel.class);
		}
		return widget;
	}
}
