/**
 * 
 */
package sk.seges.corpis.shared.domain.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import sk.seges.corpis.shared.domain.dto.BasicContactDto;
import sk.seges.corpis.shared.domain.validation.CustomerFormCheck;

/**
 * @author ladislav.gazo
 */
@Embeddable
public class JpaBasicContact extends BasicContactDto implements Serializable {
	private static final long serialVersionUID = -70900816755718583L;

	@Column
	public String getPhone() {
		return super.getPhone();
	}
	@Column
	public String getFax() {
		return super.getFax();
	}
	@Column
    @NotNull(groups = CustomerFormCheck.class)
	public String getEmail() {
		return super.getEmail();
	}
	@Column
	public String getMobile() {
		return super.getMobile();
	}
	@Column
	public String getWeb() {
		return super.getWeb();
	}

}
