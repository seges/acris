/**
 * 
 */
package sk.seges.corpis.server.domain.invoice.jpa;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import sk.seges.corpis.server.domain.customer.jpa.JpaAddress;
import sk.seges.corpis.server.domain.customer.jpa.JpaCompanyName;
import sk.seges.corpis.server.domain.customer.jpa.JpaPersonName;

/**
 * @author eldzi
 */
@Embeddable
@AttributeOverrides( {
	@AttributeOverride(name = JpaAddress.STREET, column = @Column(name = JpaDeliveryAddress.TABLE_PREFIX + JpaAddress.STREET)),
	@AttributeOverride(name = JpaAddress.CITY, column = @Column(name = JpaDeliveryAddress.TABLE_PREFIX + JpaAddress.CITY)),
	@AttributeOverride(name = JpaAddress.COUNTRY, column = @Column(name = JpaDeliveryAddress.TABLE_PREFIX + JpaAddress.COUNTRY)),
	@AttributeOverride(name = JpaAddress.STATE, column = @Column(name = JpaDeliveryAddress.TABLE_PREFIX + JpaAddress.STATE)),
	@AttributeOverride(name = JpaAddress.ZIP, column = @Column(name = JpaDeliveryAddress.TABLE_PREFIX + JpaAddress.ZIP)) })
public class JpaDeliveryAddress extends JpaAddress {
	private static final long serialVersionUID = 3303853504149160676L;

	public static final String COMPANY = "company";

	public static final String TABLE_PREFIX = "delivery_";
	
	@Embedded
	@AttributeOverrides( {
		@AttributeOverride(name = COMPANY, column = @Column(name = JpaDeliveryAddress.TABLE_PREFIX + COMPANY)),
		@AttributeOverride(name = JpaCompanyName.DEPARTMENT, column = @Column(name = JpaDeliveryAddress.TABLE_PREFIX + JpaCompanyName.DEPARTMENT)) })
	private JpaCompanyName company;
	
	@Embedded
	@AttributeOverrides( {
		@AttributeOverride(name = JpaPersonName.FIRST_NAME, column = @Column(name = JpaDeliveryAddress.TABLE_PREFIX + JpaPersonName.FIRST_NAME)),
		@AttributeOverride(name = JpaPersonName.SURNAME, column = @Column(name = JpaDeliveryAddress.TABLE_PREFIX + JpaPersonName.SURNAME)) })
	private JpaPersonName person;

	public JpaCompanyName getCompany() {
		return company;
	}

	public void setCompany(JpaCompanyName company) {
		this.company = company;
	}

	public JpaPersonName getPerson() {
		return person;
	}

	public void setPerson(JpaPersonName person) {
		this.person = person;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((person == null) ? 0 : person.hashCode());
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
		JpaDeliveryAddress other = (JpaDeliveryAddress) obj;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		if (person == null) {
			if (other.person != null)
				return false;
		} else if (!person.equals(other.person))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DeliveryAddress [company=" + company + ", person=" + person + "]";
	}
}
