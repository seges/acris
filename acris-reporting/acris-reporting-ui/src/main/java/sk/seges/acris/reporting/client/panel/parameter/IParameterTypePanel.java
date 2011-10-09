package sk.seges.acris.reporting.client.panel.parameter;

import sk.seges.acris.reporting.shared.domain.api.ReportParameterData;



/**
 * common interface for report parameters panels
 * 
 * @author marta
 *
 * @param <T>
 */
public interface IParameterTypePanel<T> {

	/**
	 * 
	 * @return selected value of parameter, e.g. Date for date parameter
	 */
	T getValue();
	
	void setValue(String t);
	
	ReportParameterData getReportParameter();
}
