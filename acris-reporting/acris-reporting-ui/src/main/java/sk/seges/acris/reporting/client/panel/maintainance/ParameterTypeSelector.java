package sk.seges.acris.reporting.client.panel.maintainance;

import sk.seges.acris.reporting.client.panel.parameter.AbstractTypePanel;
import sk.seges.acris.reporting.client.panel.parameter.CheckBoxTypePanel;
import sk.seges.acris.reporting.client.panel.parameter.DateTypePanel;
import sk.seges.acris.reporting.client.panel.parameter.ExportFileTypePanel;
import sk.seges.acris.reporting.client.panel.parameter.StringTypePanel;
import sk.seges.acris.reporting.shared.domain.api.EReportParameterType;
import sk.seges.acris.reporting.shared.domain.api.ReportParameterData;

import com.google.gwt.core.client.GWT;

/**
 * 
 * @author marta
 * 
 * @param <E>
 */
public class ParameterTypeSelector {

	protected AbstractTypePanel<?> getParamPanelInstance(ReportParameterData param) {
		AbstractTypePanel<?> widget = GWT.create(StringTypePanel.class);

		EReportParameterType enumValue;
		try {
			enumValue = Enum.valueOf(EReportParameterType.class, param.getClassName());
		} catch (IllegalArgumentException e) {
			return widget;
		}
		switch (enumValue) {
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
		case FILE_TYPE:
			return GWT.create(ExportFileTypePanel.class);
		default:
			widget = GWT.create(StringTypePanel.class);
		}

		return widget;

	}
}
