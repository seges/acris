/**
 * 
 */
package sk.seges.corpis.server.domain.customer.jpa;

import javax.persistence.AssociationOverride;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import sk.seges.corpis.server.domain.DBConstraints;
import sk.seges.corpis.server.domain.customer.server.model.base.CustomerBase;
import sk.seges.corpis.server.domain.customer.server.model.data.CustomerData;
import sk.seges.corpis.server.domain.server.model.data.AddressData;
import sk.seges.corpis.shared.domain.validation.customer.CompanyCustomerFormCheck;

/**
 * @author ladislav.gazo
 */
@Entity
@SequenceGenerator(name = "seqCustomers", sequenceName = "SEQ_CUSTOMERS", initialValue = 1)
@Table(name = "customer")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorValue(value = "1")
@AssociationOverride(name = CustomerData.CORRESP_ADDRESS + "." + AddressData.COUNTRY, joinColumns = @JoinColumn(name = DBConstraints.CORRESP_TABLE_PREFIX
			+ AddressData.COUNTRY + "_id"))
public class JpaCustomerBase extends CustomerBase {
	private static final long serialVersionUID = -7070969291047080272L;

	public JpaCustomerBase() {
		company = new JpaCompanyName();
		person = new JpaPersonName();
		address = new JpaAddress();
		contact = new JpaBasicContact();
	}

	private Integer version;

	@Id
	@GeneratedValue(generator = "seqCustomers")
	public Integer getId() {
		return super.getId();
	}

	@Column(name = "CONTACT_PERSON")
	public String getContactPerson() {
		return super.getContactPerson();
	}

	@Column(length = DBConstraints.SHORTCUT_LENGTH)
	public String getShortcut() {
		return super.getShortcut();
	}

	@Column(name = "DIC")
	public String getDic() {
		return super.getDic();
	}

	@Column(name = "IC_DPH")
	public String getIcDph() {
		return super.getIcDph();
	}

	@Column(name = "ICO")
	@NotNull(groups = CompanyCustomerFormCheck.class)
	@Size(min = 1, groups = CompanyCustomerFormCheck.class)
	public String getIco() {
		return super.getIco();
	}

	@Column(name = "INVOICE_PAYMENT_INTERVAL")
	public Short getInvoicePaymentInterval() {
		return super.getInvoicePaymentInterval();
	}

	@Column(name = "ACCOUNT_NUMBER")
	public String getAccountNumber() {
		return super.getAccountNumber();
	}

	@Column(name = "TAX_PAYMENT")
	public Boolean getTaxPayment() {
		return super.getTaxPayment();
	}

	@Override
	public Boolean getCompanyType() {
		return super.getCompanyType();
	}

	@Version
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@AttributeOverride(name = JpaCompanyName.COMPANY_NAME, column = @Column(unique = true, nullable = true))
	@Embedded
	@Valid
	public JpaCompanyName getCompany() {
		return (JpaCompanyName) super.getCompany();
	}

	@Embedded
	@Valid
	public JpaPersonName getPerson() {
		return (JpaPersonName) super.getPerson();
	}

	@Embedded
	@Valid
	public JpaAddress getAddress() {
		return (JpaAddress) super.getAddress();
	}

	@Embedded
	@Valid
	public JpaBasicContact getContact() {
		return (JpaBasicContact) super.getContact();
	}

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = JpaCompanyName.COMPANY_NAME, column = @Column(name = DBConstraints.CORRESP_TABLE_PREFIX
					+ JpaCompanyName.COMPANY_NAME)),
			@AttributeOverride(name = JpaCompanyName.DEPARTMENT, column = @Column(name = DBConstraints.CORRESP_TABLE_PREFIX
					+ JpaCompanyName.DEPARTMENT)) })
	public JpaCompanyName getCorrespCompany() {
		return (JpaCompanyName) super.getCorrespCompany();
	}

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = JpaPersonName.SALUTATION, column = @Column(name = DBConstraints.CORRESP_TABLE_PREFIX
					+ JpaPersonName.SALUTATION)),
			@AttributeOverride(name = JpaPersonName.FIRST_NAME, column = @Column(name = DBConstraints.CORRESP_TABLE_PREFIX
					+ JpaPersonName.FIRST_NAME)),
			@AttributeOverride(name = JpaPersonName.SURNAME, column = @Column(name = DBConstraints.CORRESP_TABLE_PREFIX
					+ JpaPersonName.SURNAME)) })
	public JpaPersonName getCorrespPerson() {
		return (JpaPersonName) super.getCorrespPerson();
	}

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = JpaAddress.STREET, column = @Column(name = DBConstraints.CORRESP_TABLE_PREFIX + JpaAddress.STREET)),
			@AttributeOverride(name = JpaAddress.CITY, column = @Column(name = DBConstraints.CORRESP_TABLE_PREFIX + JpaAddress.CITY)),
			@AttributeOverride(name = JpaAddress.STATE, column = @Column(name = DBConstraints.CORRESP_TABLE_PREFIX + JpaAddress.STATE)),
			@AttributeOverride(name = JpaAddress.ZIP, column = @Column(name = DBConstraints.CORRESP_TABLE_PREFIX + JpaAddress.ZIP, length = DBConstraints.ZIP_LENGTH)),
			@AttributeOverride(name = JpaAddress.COUNTRY, column = @Column(name = DBConstraints.CORRESP_TABLE_PREFIX + JpaAddress.COUNTRY))})
	@AssociationOverride(name = JpaAddress.COUNTRY, joinColumns = @JoinColumn(name = DBConstraints.CORRESP_TABLE_PREFIX
			+ JpaAddress.COUNTRY + "_id"))
	public JpaAddress getCorrespAddress() {
		return (JpaAddress) super.getCorrespAddress();
	}

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = JpaBasicContact.PHONE, column = @Column(name = DBConstraints.CORRESP_TABLE_PREFIX
					+ JpaBasicContact.PHONE)),
			@AttributeOverride(name = JpaBasicContact.FAX, column = @Column(name = DBConstraints.CORRESP_TABLE_PREFIX + JpaBasicContact.FAX)),
			@AttributeOverride(name = JpaBasicContact.EMAIL, column = @Column(name = DBConstraints.CORRESP_TABLE_PREFIX
					+ JpaBasicContact.EMAIL)),
			@AttributeOverride(name = JpaBasicContact.MOBILE, column = @Column(name = DBConstraints.CORRESP_TABLE_PREFIX
					+ JpaBasicContact.MOBILE)),
			@AttributeOverride(name = JpaBasicContact.WEB, column = @Column(name = DBConstraints.CORRESP_TABLE_PREFIX + JpaBasicContact.WEB)) })
	public JpaBasicContact getCorrespContact() {
		return (JpaBasicContact) super.getCorrespContact();
	}


}
