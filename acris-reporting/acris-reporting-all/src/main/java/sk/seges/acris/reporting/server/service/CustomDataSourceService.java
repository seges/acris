package sk.seges.acris.reporting.server.service;

import java.util.Map;

import net.sf.jasperreports.engine.JRParameter;
import sk.seges.acris.reporting.server.datasource.CustomDataSource;

import com.jaspersoft.jasperserver.api.metadata.jasperreports.service.ReportDataSourceService;

/**
 * 
 * this service serves only for setting of JRParameter.REPORT_DATA_SOURCE, but
 * is necessary for JasperServer <br />
 * it can be extended if some action with parameters is needed the service is
 * used in {@link CustomJavaBeanDataSourceFactory}
 * 
 * @author marta
 * 
 * @param <T>
 *            type of bean which is used as a row in JasperReport
 */
public class CustomDataSourceService<T> implements ReportDataSourceService {

	private CustomDataSource<T> customDataSource;

	public CustomDataSourceService() {}

	public CustomDataSourceService(CustomDataSource<T> customDataSource) {
		super();
		this.init(customDataSource);
	}

	public void init(CustomDataSource<T> source) {
		this.customDataSource = source;
	}

	/**
	 * method is final because parameter REPORT_DATA_SOURCE must be set
	 * additional parameters can be set via setExtendedReportParameterValues
	 * 
	 */
	public final void setReportParameterValues(Map parameters) {
		
		try {
			this.setExtendedReportParameterValues(parameters);
			parameters.put(JRParameter.REPORT_DATA_SOURCE, customDataSource);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * if this method is overridden, some action with parameters can be
	 * provided, all parameters has to be provided as parameters in jrxml file
	 * and also as input controls in jasperserver repository
	 * 
	 * @param Map
	 *            of parameters of report
	 */
	protected void setExtendedReportParameterValues(Map parameters) throws Exception {
		return;
	}

	public void closeConnection() {
	// TODO: add connection close support. do not need this...
	}

	public void setCustomDataSource(CustomDataSource<T> customDataSource) {
		this.customDataSource = customDataSource;
	}

	public CustomDataSource<T> getCustomDataSource() {
		return customDataSource;
	}

}
