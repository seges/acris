/**
 * 
 */
package sk.seges.corpis.shared.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import sk.seges.corpis.shared.domain.api.DBConstraints;
import sk.seges.corpis.shared.domain.dto.CountryDto;

/**
 * @author ladislav.gazo
 */
@Entity
@Table(name = "country")
@SequenceGenerator(name = "country_id_seq", sequenceName = "country_id_seq", initialValue = 1)
public class JpaCountry extends CountryDto<Integer> {
	private static final long serialVersionUID = 8388027517830828966L;
	
	@Id
	@GeneratedValue(generator="country_id_seq")
	@Override
	public Integer getId() {
		return super.getId();
	}
	
	@Override
	public String getLabel() {
		return super.getLabel();
	}
	
	@Override
	@Column(length = DBConstraints.DOMAIN_LENGTH)
	public String getDomain() {
		return super.getDomain();
	}

	@Override
	@Column(unique = true, length = DBConstraints.COUNTRY_LENGTH)
	public String getCountry() {
		return super.getCountry();
	}
	
	@Override
	@Column(length = DBConstraints.LANGUAGE_LENGTH, name = "lang")
	public String getLanguage() {
		return super.getLanguage();
	}

}
