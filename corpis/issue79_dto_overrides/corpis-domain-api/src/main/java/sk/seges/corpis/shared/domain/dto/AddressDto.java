/**
 * 
 */
package sk.seges.corpis.shared.domain.dto;

import sk.seges.corpis.shared.domain.api.AddressData;
import sk.seges.corpis.shared.domain.api.CountryData;

/**
 * @author ladislav.gazo
 */
public class AddressDto implements AddressData {
	private static final long serialVersionUID = 2180417028349836062L;
	
	protected String city;
	protected CountryData<?> country;
	protected String state;
	protected String street;
	protected String zip;
		
	@Override
	public String getCity() {
		return city;
	}

	@Override
	public CountryData<?> getCountry() {
		return country;
	}

	@Override
	public String getState() {
		return state;
	}

	@Override
	public String getStreet() {
		return street;
	}

	@Override
	public String getZip() {
		return zip;
	}

	@Override
	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public void setCountry(CountryData<?> country) {
		this.country = country;
	}

	@Override
	public void setState(String state) {
		this.state = state;
	}

	@Override
	public void setStreet(String street) {
		this.street = street;
	}

	@Override
	public void setZip(String zip) {
		this.zip = zip;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((street == null) ? 0 : street.hashCode());
		result = prime * result + ((zip == null) ? 0 : zip.hashCode());
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
		AddressDto other = (AddressDto) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (street == null) {
			if (other.street != null)
				return false;
		} else if (!street.equals(other.street))
			return false;
		if (zip == null) {
			if (other.zip != null)
				return false;
		} else if (!zip.equals(other.zip))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "AddressDto [city=" + city + ", country=" + country + ", state=" + state + ", street="
				+ street + ", zip=" + zip + "]";
	}

}
