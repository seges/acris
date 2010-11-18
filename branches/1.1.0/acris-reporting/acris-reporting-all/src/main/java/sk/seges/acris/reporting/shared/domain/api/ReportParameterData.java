/**
 * 
 */
package sk.seges.acris.reporting.shared.domain.api;

import sk.seges.sesam.domain.IMutableDomainObject;

/**
 * @author ladislav.gazo
 */
public interface ReportParameterData extends IMutableDomainObject<Long>{
	Integer getOrderNumber();
	void setOrderNumber(Integer orderNumber);
	
	String getName();
	void setName(String name);
	
	String getDisplayedName();
	void setDisplayedName(String displayedName);
	
	String getDescription();
	void setDescription(String description);
	
	String getClassName();
	void setClassName(String className);
	
	Boolean getHidden();
	void setHidden(Boolean hidden);
	
	ReportParameterData getParent();
	void setParent(ReportParameterData parent);
}
