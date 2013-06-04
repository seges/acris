package sk.seges.acris.reporting.shared.service;

import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * 
 * @author marta
 *
 */
public interface IReportingService extends RemoteService {

	String exportReport(Long reportDescriptionId, String exportType, Map<String, Object> parameters, String webId);
	
	String exportReport(Long reportDescriptionId, String exportType, Map<String, Object> parameters, String webId, String reportName);
	
	String exportReportToHtml(Long reportDescriptionId, Map<String, Object> parameters);
}