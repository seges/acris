package sk.seges.acris.reporting.rpc.service;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IReportingServiceAsync {
	
	void exportReport(Long reportDescriptionId, String exportType, Map<String, Object> parameters, String webId, AsyncCallback<String> callback);

	void exportReportToHtml(Long reportDescriptionId, Map<String, Object> parameters, AsyncCallback<String> callback);

}
