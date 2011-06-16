package sk.seges.corpis.shared.domain.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import sk.seges.corpis.shared.domain.api.DBConstraints;
import sk.seges.corpis.shared.domain.api.Salutation;
import sk.seges.corpis.shared.domain.dto.PersonNameDto;
import sk.seges.corpis.shared.domain.validation.PersonCustomerFormCheck;

/**
 * @author ladislav.gazo
 */
@Embeddable
public class JpaPersonName extends PersonNameDto implements Serializable {
	private static final long serialVersionUID = 4913258430027481964L;

	@Column
	@NotNull(groups = PersonCustomerFormCheck.class)
	@Size(min = 1, max = DBConstraints.PERSON_LENGTH, groups = PersonCustomerFormCheck.class)
	public String getFirstName() {
		return super.getFirstName();
	}

	@Column
	@NotNull(groups = PersonCustomerFormCheck.class)
	@Size(min = 1, max = DBConstraints.PERSON_LENGTH, groups = PersonCustomerFormCheck.class)
	public String getSurname() {
		return super.getSurname();
	}

	@Enumerated
	public Salutation getSalutation() {
		return super.getSalutation();
	}

}
