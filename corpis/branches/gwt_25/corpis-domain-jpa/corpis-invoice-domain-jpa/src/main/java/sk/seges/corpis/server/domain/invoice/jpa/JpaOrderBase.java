/**
 * 
 */
package sk.seges.corpis.server.domain.invoice.jpa;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.Valid;

import sk.seges.corpis.server.domain.customer.jpa.JpaAddress;
import sk.seges.corpis.server.domain.customer.jpa.JpaBasicContact;
import sk.seges.corpis.server.domain.customer.jpa.JpaCustomerCore;
import sk.seges.corpis.server.domain.invoice.server.model.base.OrderCoreBase;
import sk.seges.corpis.server.domain.invoice.server.model.data.DeliveryPersonData;
import sk.seges.corpis.server.domain.invoice.server.model.data.OrderCoreData;
import sk.seges.corpis.server.domain.invoice.server.model.data.OrderStatusData;
import sk.seges.corpis.server.domain.jpa.JpaCurrency;
import sk.seges.corpis.server.domain.server.model.data.AddressData;
import sk.seges.corpis.server.domain.server.model.data.BasicContactData;
import sk.seges.corpis.shared.domain.EPaymentType;
import sk.seges.corpis.shared.domain.invoice.ETransports;

/**
 * @author eldzi
 */
@MappedSuperclass
public abstract class JpaOrderBase extends OrderCoreBase implements OrderCoreData {

	private static final long serialVersionUID = -6186188601422302822L;

	public JpaOrderBase() {
		setDeliveryContact(new JpaBasicContact());
		setDeliveryPerson(new JpaDeliveryPerson());
		setDeliveryAddress(new JpaAddress());
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public JpaCustomerCore getCustomer() {
		return (JpaCustomerCore) super.getCustomer();
	}

	@Column(name = "creation_date")
	public Date getCreationDate() {
		return super.getCreationDate();
	}

	@ManyToOne
	public JpaCurrency getCurrency() {
		return (JpaCurrency) super.getCurrency();
	}

	@Column
	public String getOrderId() {
		return super.getOrderId();
	}

	@Column(name = "note")
	public String getNote() {
		return super.getNote();
	}

	@Column(name = "delivered_by")
	public ETransports getDeliveredBy() {
		return super.getDeliveredBy();
	}

	@Deprecated
	@Column(name = "delivery_price")
	public Double getDeliveryPrice() {
		return super.getDeliveryPrice();
	}

	@Column(name = "tracking_number")
	public String getTrackingNumber() {
		return super.getTrackingNumber();
	}

	@ManyToOne(targetEntity = JpaOrderStatus.class)
	public OrderStatusData getStatus() {
		return super.getStatus();
	}

	@Embedded
	@Valid
	@AttributeOverrides( {
		@AttributeOverride(name = AddressData.STREET, column = @Column(name = JpaDeliveryPerson.TABLE_PREFIX + AddressData.STREET)),
		@AttributeOverride(name = AddressData.CITY, column = @Column(name = JpaDeliveryPerson.TABLE_PREFIX + AddressData.CITY)),
		@AttributeOverride(name = AddressData.COUNTRY, column = @Column(name = JpaDeliveryPerson.TABLE_PREFIX + AddressData.COUNTRY)),
		@AttributeOverride(name = AddressData.STATE, column = @Column(name = JpaDeliveryPerson.TABLE_PREFIX + AddressData.STATE)),
		@AttributeOverride(name = AddressData.ZIP, column = @Column(name = JpaDeliveryPerson.TABLE_PREFIX + AddressData.ZIP)) })
	public JpaAddress getDeliveryAddress() {
		return (JpaAddress) super.getDeliveryAddress();
	}

	@Embedded
	@AttributeOverrides( {
			@AttributeOverride(name = BasicContactData.PHONE, column = @Column(name = JpaDeliveryPerson.TABLE_PREFIX + BasicContactData.PHONE)),
			@AttributeOverride(name = BasicContactData.FAX, column = @Column(name = JpaDeliveryPerson.TABLE_PREFIX + BasicContactData.FAX)),
			@AttributeOverride(name = BasicContactData.EMAIL, column = @Column(name = JpaDeliveryPerson.TABLE_PREFIX + BasicContactData.EMAIL)),
			@AttributeOverride(name = BasicContactData.MOBILE, column = @Column(name = JpaDeliveryPerson.TABLE_PREFIX + BasicContactData.MOBILE)),
			@AttributeOverride(name = BasicContactData.WEB, column = @Column(name = JpaDeliveryPerson.TABLE_PREFIX + BasicContactData.WEB)) })
	public JpaBasicContact getDeliveryContact() {
		return (JpaBasicContact) super.getDeliveryContact();
	}

	@Column(name = "ico")
	public String getIco() {
		return super.getIco();
	}

	@Column(name = "icdph")
	public String getIcDph() {
		return super.getIcDph();
	}

	@Column
	public EPaymentType getPaymentType() {
		return super.getPaymentType();
	}

	@Column(name = "account_number")
	public String getAccountNumber() {
		return super.getAccountNumber();
	}

	@Column(name = "project_number")
	public String getProjectNumber() {
		return super.getProjectNumber();
	}

	@Embedded
	@AttributeOverrides( {
		@AttributeOverride(name = DeliveryPersonData.COMPANY, column = @Column(name = JpaDeliveryPerson.TABLE_PREFIX + DeliveryPersonData.COMPANY)),
		@AttributeOverride(name = DeliveryPersonData.PERSON, column = @Column(name = JpaDeliveryPerson.TABLE_PREFIX + DeliveryPersonData.PERSON)) })
	public DeliveryPersonData getDeliveryPerson() {
		return super.getDeliveryPerson();
	}

	@Column(name = "same_delivery_address")
	public Boolean getSameDeliveryAddress() {
		return super.getSameDeliveryAddress();
	}
}