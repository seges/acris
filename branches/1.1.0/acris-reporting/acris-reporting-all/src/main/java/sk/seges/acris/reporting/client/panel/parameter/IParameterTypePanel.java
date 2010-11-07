package sk.seges.acris.reporting.client.panel.parameter;

import sk.seges.acris.reporting.rpc.domain.ReportParameter;



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
	
	ReportParameter getReportParameter();
}
