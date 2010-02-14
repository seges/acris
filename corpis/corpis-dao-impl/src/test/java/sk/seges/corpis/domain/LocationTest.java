/**
 * 
 */
package sk.seges.corpis.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import sk.seges.sesam.domain.IDomainObject;

/**
 * @author eldzi
 */
@Entity
@Table(name = "locations")
public class LocationTest implements IDomainObject<Long> {
	private static final long serialVersionUID = -1805706327802594676L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	@ManyToOne(cascade = CascadeType.PERSIST)
	private StreetTest StreetTest;
	private String city;
	private String state;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StreetTest getStreet() {
		return StreetTest;
	}

	public void setStreet(StreetTest StreetTest) {
		this.StreetTest = StreetTest;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((StreetTest == null) ? 0 : StreetTest.hashCode());
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
		LocationTest other = (LocationTest) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (StreetTest == null) {
			if (other.StreetTest != null)
				return false;
		} else if (!StreetTest.equals(other.StreetTest))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Location [city=" + city + ", id=" + id + ", state=" + state
				+ ", StreetTest=" + (StreetTest == null ? "n/a" : StreetTest) + "]";
	}
}
