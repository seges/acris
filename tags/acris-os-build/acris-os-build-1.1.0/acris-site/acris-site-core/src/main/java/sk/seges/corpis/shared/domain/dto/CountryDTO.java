package sk.seges.corpis.shared.domain.dto;

import sk.seges.corpis.shared.domain.api.CountryData;

public class CountryDTO implements CountryData {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String label;
	private Boolean europeanUnion;

	private String domain;

	private String country;

	private String language;

	public CountryDTO() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Boolean isEuropeanUnion() {
		return europeanUnion;
	}

	public void setEuropeanUnion(Boolean europeanUnion) {
		this.europeanUnion = europeanUnion;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		CountryData other = (CountryData) obj;
		if (country == null) {
			if (other.getCountry() != null) return false;
		} else if (!country.equals(other.getCountry())) return false;
		return true;
	}

	@Override
	public String toString() {
		return "CountryDTO [id=" + id + ", label=" + label + ", europeanUnion=" + europeanUnion + ", domain=" + domain + ", country=" + country + ", language="
				+ language + "]";
	}
}