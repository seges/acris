/**
 * 
 */
package sk.seges.corpis.shared.domain.customer.base;

import sk.seges.corpis.shared.domain.customer.api.CompanyNameData;
import sk.seges.corpis.shared.domain.customer.base.CompanyNameBase;


/**
 * @author ladislav.gazo
 */
public class CompanyNameBase implements CompanyNameData {
	private static final long serialVersionUID = 6214819202478310754L;
	
	protected String companyName;
	protected String department;
	
	@Override
	public String getCompanyName() {
		return companyName;
	}

	@Override
	public String getDepartment() {
		return department;
	}

	@Override
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Override
	public void setDepartment(String department) {
		this.department = department;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyName == null) ? 0 : companyName.hashCode());
		result = prime * result + ((department == null) ? 0 : department.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompanyNameBase other = (CompanyNameBase) obj;
		if (companyName == null) {
			if (other.companyName != null)
				return false;
		} else if (!companyName.equals(other.companyName))
			return false;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CompanyNameDto [companyName=" + companyName + ", department=" + department + "]";
	}
	
}
