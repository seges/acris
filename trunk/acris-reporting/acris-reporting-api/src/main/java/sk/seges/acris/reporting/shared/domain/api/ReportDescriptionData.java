/**
 * 
 */
package sk.seges.acris.reporting.shared.domain.api;

import java.util.Date;
import java.util.List;

import sk.seges.sesam.domain.IMutableDomainObject;

/**
 * @author ladislav.gazo
 */
public interface ReportDescriptionData extends IMutableDomainObject<Long> {
	public static final String NAME_ATTR = "name";
	public static final String CREATION_DATE_ATTR = "creationDate";
	public static final String ID_ATTR = "id";

	String getName();
	void setName(String name);
	
	String getDescription();
	void setDescription(String description);
	
	Date getCreationDate();
	void setCreationDate(Date creationDate);
	
	String getReportUrl();
	void setReportUrl(String reportUrl);
	
	List<ReportParameterData> getParametersList();
	void setParametersList(List<ReportParameterData> parametersList);
	
	String getDisplayName();
	void setDisplayName(String displayName);
	
}
