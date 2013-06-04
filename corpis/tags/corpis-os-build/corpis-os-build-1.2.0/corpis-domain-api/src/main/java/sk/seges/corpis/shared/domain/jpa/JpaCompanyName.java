/**
 * 
 */
package sk.seges.corpis.shared.domain.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import sk.seges.corpis.shared.domain.api.DBConstraints;
import sk.seges.corpis.shared.domain.dto.CompanyNameDto;
import sk.seges.corpis.shared.domain.validation.CompanyCustomerFormCheck;

/**
 * @author ladislav.gazo
 */
@Embeddable
public class JpaCompanyName extends CompanyNameDto implements Serializable {
	private static final long serialVersionUID = 1517041015021653542L;
	
	@Column(length = DBConstraints.COMPANY_LENGTH)
	@NotNull(groups = CompanyCustomerFormCheck.class)
	@Size(min = 1, max = DBConstraints.COMPANY_LENGTH, groups = CompanyCustomerFormCheck.class)
	public String getCompanyName() {
		return super.getCompanyName();
	}

	@Column
	public String getDepartment() {
		return super.getDepartment();
	}

}
