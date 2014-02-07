package sk.seges.acris.reporting.shared.service;

import com.google.gwt.user.client.rpc.RemoteService;
import sk.seges.sesam.pap.service.annotation.RemoteServiceDefinition;

import java.util.Map;

/**
 * 
 * @author marta
 *
 */
@RemoteServiceDefinition
public interface IReportingService extends RemoteService {

	String exportReport(Long reportDescriptionId, String exportType, Map<String, String> parameters, String webId);

	String exportReportToHtml(Long reportDescriptionId, Map<String, String> parameters);
}