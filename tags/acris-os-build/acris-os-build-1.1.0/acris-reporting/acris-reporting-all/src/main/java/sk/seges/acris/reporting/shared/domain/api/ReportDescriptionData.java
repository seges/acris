/**
 * 
 */
package sk.seges.acris.reporting.shared.domain.api;

import java.util.Date;
import java.util.List;

import sk.seges.acris.binding.client.annotations.BeanWrapper;
import sk.seges.acris.reporting.rpc.domain.ReportParameter;
import sk.seges.sesam.domain.IMutableDomainObject;

/**
 * @author ladislav.gazo
 */
@BeanWrapper
public interface ReportDescriptionData extends IMutableDomainObject<Long> {
	String getName();
	void setName(String name);
	
	String getDescription();
	void setDescription(String description);
	
	Date getCreationDate();
	void setCreationDate(Date creationDate);
	
	String getReportUrl();
	void setReportUrl(String reportUrl);
	
	List<ReportParameter> getParametersList();
	void setParametersList(List<ReportParameter> parametersList);	
}
