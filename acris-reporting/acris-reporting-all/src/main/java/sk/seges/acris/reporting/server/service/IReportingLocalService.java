package sk.seges.acris.reporting.server.service;

import java.util.Map;

public interface IReportingLocalService extends IReportingServiceLocal {

	String exportReport(Long reportDescriptionId, String exportType, Map<String, Object> parameters, String webId, String reportName);
}
