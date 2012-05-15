/**
 * 
 */
package sk.seges.corpis.server.domain.invoice.jpa;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;

import sk.seges.corpis.server.domain.customer.jpa.JpaAddress;
import sk.seges.corpis.server.domain.customer.jpa.JpaBasicContact;
import sk.seges.corpis.server.domain.customer.jpa.JpaCompanyName;
import sk.seges.corpis.server.domain.customer.jpa.JpaPersonName;
import sk.seges.corpis.server.domain.invoice.server.model.data.DeliveryPersonData;
import sk.seges.corpis.server.domain.invoice.server.model.data.OrderData;
import sk.seges.corpis.server.domain.server.model.data.AddressData;
import sk.seges.corpis.server.domain.server.model.data.BasicContactData;
import sk.seges.corpis.server.domain.server.model.data.CompanyNameData;
import sk.seges.corpis.server.domain.server.model.data.PersonNameData;
import sk.seges.corpis.shared.domain.invoice.EOrderStatus;
import sk.seges.corpis.shared.domain.invoice.EPaymentType;
import sk.seges.corpis.shared.domain.invoice.ETransports;

/**
 * @author eldzi
 */
@MappedSuperclass
public abstract class JpaOrderBase extends JpaAccountable implements OrderData {

	private static final long serialVersionUID = -6186188601422302822L;

	public static final String ORDER_ID = "orderId";

	private String orderId;

	private String note;

	private ETransports deliveredBy;

	private Double deliveryPrice;

	private String trackingNumber;

	private EOrderStatus status;

	private CompanyNameData company;
	
	private PersonNameData person;
	
	private AddressData address;
	
//	private AddressData deliveryAddress;

	private BasicContactData contact;

	private BasicContactData deliveryContact;

	private String ico;

	private String icDph;
	
	private EPaymentType paymentType;

	private String accountNumber;

	private String projectNumber;
		
	private DeliveryPersonData deliveryPerson;

	private Boolean sameDeliveryAddress;

	@Column
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Column(name = "Note")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "delivered_by")
	public ETransports getDeliveredBy() {
		return deliveredBy;
	}

	public void setDeliveredBy(ETransports deliveredBy) {
		this.deliveredBy = deliveredBy;
	}

	@Column(name = "delivery_price")
	public Double getDeliveryPrice() {
		return deliveryPrice;
	}

	public void setDeliveryPrice(Double deliveryPrice) {
		this.deliveryPrice = deliveryPrice;
	}

	@Column(name = "tracking_number")
	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	@Column(name = "status")
	public EOrderStatus getStatus() {
		return status;
	}

	public void setStatus(EOrderStatus status) {
		this.status = status;
	}

	@Embedded
	public JpaCompanyName getCompany() {
		return (JpaCompanyName) company;
	}

	public void setCompany(CompanyNameData company) {
		this.company = company;
	}

	@Embedded
	public JpaPersonName getPerson() {
		return (JpaPersonName) person;
	}

	public void setPerson(PersonNameData person) {
		this.person = person;
	}

	@Embedded
	public JpaAddress getAddress() {
		return (JpaAddress) address;
	}

	public void setAddress(AddressData address) {
		this.address = address;
	}

//	@Embedded
//	@Valid
//	@AttributeOverrides( {
//		@AttributeOverride(name = AddressData.STREET, column = @Column(name = JpaDeliveryPerson.TABLE_PREFIX + AddressData.STREET)),
//		@AttributeOverride(name = AddressData.CITY, column = @Column(name = JpaDeliveryPerson.TABLE_PREFIX + AddressData.CITY)),
//		@AttributeOverride(name = AddressData.COUNTRY, column = @Column(name = JpaDeliveryPerson.TABLE_PREFIX + AddressData.COUNTRY)),
//		@AttributeOverride(name = AddressData.STATE, column = @Column(name = JpaDeliveryPerson.TABLE_PREFIX + AddressData.STATE)),
//		@AttributeOverride(name = AddressData.ZIP, column = @Column(name = JpaDeliveryPerson.TABLE_PREFIX + AddressData.ZIP)) })
//	public JpaAddress getDeliveryAddress() {
//		return (JpaAddress) deliveryAddress;
//	}

//	public void setDeliveryAddress(AddressData deliveryAddress) {
//		this.deliveryAddress = deliveryAddress;
//	}

	@Embedded
	public JpaBasicContact getContact() {
		return (JpaBasicContact) contact;
	}

	public void setContact(BasicContactData contact) {
		this.contact = contact;
	}

	@Embedded
	@AttributeOverrides( {
			@AttributeOverride(name = BasicContactData.PHONE, column = @Column(name = JpaDeliveryPerson.TABLE_PREFIX + BasicContactData.PHONE)),
			@AttributeOverride(name = BasicContactData.FAX, column = @Column(name = JpaDeliveryPerson.TABLE_PREFIX + BasicContactData.FAX)),
			@AttributeOverride(name = BasicContactData.EMAIL, column = @Column(name = JpaDeliveryPerson.TABLE_PREFIX + BasicContactData.EMAIL)),
			@AttributeOverride(name = BasicContactData.MOBILE, column = @Column(name = JpaDeliveryPerson.TABLE_PREFIX + BasicContactData.MOBILE)),
			@AttributeOverride(name = BasicContactData.WEB, column = @Column(name = JpaDeliveryPerson.TABLE_PREFIX + BasicContactData.WEB)) })
	public JpaBasicContact getDeliveryContact() {
		return (JpaBasicContact) deliveryContact;
	}

	public void setDeliveryContact(BasicContactData deliveryContact) {
		this.deliveryContact = deliveryContact;
	}

	@Column(name = "ICO")
	public String getIco() {
		return ico;
	}

	public void setIco(String ico) {
		this.ico = ico;
	}

	@Column(name = "ICDPH")
	public String getIcDph() {
		return icDph;
	}

	public void setIcDph(String icDph) {
		this.icDph = icDph;
	}

	public EPaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(EPaymentType paymentType) {
		this.paymentType = paymentType;
	}

	@Column(name = "ACCOUNT_NUMBER")
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	@Column(name = "PROJECT_NUMBER")
	public String getProjectNumber() {
		return projectNumber;
	}

	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}

	@Embedded
	@AttributeOverrides( {
		@AttributeOverride(name = DeliveryPersonData.COMPANY, column = @Column(name = JpaDeliveryPerson.TABLE_PREFIX + DeliveryPersonData.COMPANY)),
		@AttributeOverride(name = DeliveryPersonData.PERSON, column = @Column(name = JpaDeliveryPerson.TABLE_PREFIX + DeliveryPersonData.PERSON)) })
	public DeliveryPersonData getDeliveryPerson() {
		return deliveryPerson;
	}

	public void setDeliveryPerson(DeliveryPersonData deliveryPerson) {
		this.deliveryPerson = deliveryPerson;
	}

	@Column(name = "SAME_DELIVERY_ADDRESS")
	public Boolean getSameDeliveryAddress() {
		return sameDeliveryAddress;
	}

	@Override
	public void setSameDeliveryAddress(Boolean sameDeliveryAddress) {
		this.sameDeliveryAddress = sameDeliveryAddress;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
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
		JpaOrderBase other = (JpaOrderBase) obj;
		if (orderId == null) {
			if (other.orderId != null)
				return false;
		} else if (!orderId.equals(other.orderId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Order [accountNumber=" + accountNumber + ", address=" + address + ", company=" + company
				+ ", contact=" + contact + ", deliveredBy=" + deliveredBy + ", deliveryAddress="
//				+ deliveryAddress + ", deliveryContact=" + deliveryContact + ", deliveryPrice="
				+ deliveryPrice + ", icDph=" + icDph + ", ico=" + ico + ", id=" + getId() + ", note=" + note
				+ ", orderId=" + orderId + ", paymentType=" + paymentType
				+ ", person=" + person + ", projectNumber=" + projectNumber + ", status=" + status
				+ ", trackingNumber=" + trackingNumber + "]";
	}
}