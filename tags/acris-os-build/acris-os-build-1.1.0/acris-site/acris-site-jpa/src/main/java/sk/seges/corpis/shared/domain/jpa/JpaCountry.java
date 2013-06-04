package sk.seges.corpis.shared.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import sk.seges.acris.domain.shared.domain.api.DBConstraints;
import sk.seges.corpis.shared.domain.dto.CountryDTO;

@Entity
@Table(name = "country")
public class JpaCountry extends CountryDTO {

	private static final long serialVersionUID = 2L;

	public JpaCountry() {
	}
	
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return super.getId();
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