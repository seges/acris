/**
 * 
 */
package sk.seges.corpis.shared.domain.customer.api;

import java.io.Serializable;


/**
 * @author ladislav.gazo
 */
public interface CompanyNameData extends Serializable {
	public static final String COMPANY_NAME = "companyName";
	public static final String DEPARTMENT = "department";

	String getCompanyName();
	void setCompanyName(String companyName);

	String getDepartment();
	void setDepartment(String department);
}
