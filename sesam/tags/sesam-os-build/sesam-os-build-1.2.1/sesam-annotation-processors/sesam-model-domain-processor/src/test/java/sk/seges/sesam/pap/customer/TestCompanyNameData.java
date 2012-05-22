/**
 * 
 */
package sk.seges.sesam.pap.customer;

import java.io.Serializable;


/**
 * @author ladislav.gazo
 */
public interface TestCompanyNameData extends Serializable {
	public static final String COMPANY_NAME = "companyName";
	public static final String DEPARTMENT = "department";

	String getCompanyName();
	void setCompanyName(String companyName);

	String getDepartment();
	void setDepartment(String department);
}
