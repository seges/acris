package sk.seges.acris.reporting.rpc.service;

import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * 
 * @author marta
 *
 */
public interface IReportingService extends RemoteService {

	String exportReport(Long reportId, String exportType, Map<String, Object> parameters, String webId);
	
	String exportReportToHtml(Long reportId, Map<String, Object> parameters);
}