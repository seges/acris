package sk.seges.corpis.server.domain.customer.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import sk.seges.corpis.server.domain.DBConstraints;
import sk.seges.corpis.server.domain.server.model.base.AddressBase;
import sk.seges.corpis.shared.domain.validation.customer.CustomerFormCheck;
import sk.seges.corpis.shared.domain.validation.customer.OrderDeliveryCheck;

/**
 * @author ladislav.gazo
 */
@Embeddable
public class JpaAddress extends AddressBase implements Serializable {
	private static final long serialVersionUID = 2682133037971063332L;

	@Column
	@NotNull(groups = {CustomerFormCheck.class, OrderDeliveryCheck.class})
	@Size(min = 1, groups = {CustomerFormCheck.class, OrderDeliveryCheck.class})
	public String getStreet() {
		return super.getStreet();
	}
	@Column
	@NotNull(groups = {CustomerFormCheck.class, OrderDeliveryCheck.class})
	@Size(min = 1, groups = {CustomerFormCheck.class, OrderDeliveryCheck.class})
	public String getCity() {
		return super.getCity();
	}
	
	@ManyToOne(targetEntity = JpaCountry.class)
	@NotNull(groups = {CustomerFormCheck.class, OrderDeliveryCheck.class})
	public JpaCountry getCountry() {
		return (JpaCountry) super.getCountry();
	}
	
	@Column
	public String getState() {
		return super.getState();
	}
	
	@Column(length = DBConstraints.ZIP_LENGTH)
	@NotNull(groups = {CustomerFormCheck.class, OrderDeliveryCheck.class})
	@Size(min = 1, max = DBConstraints.ZIP_LENGTH, groups = {CustomerFormCheck.class, OrderDeliveryCheck.class})
	public String getZip() {
		return super.getZip();
	}
	
}
