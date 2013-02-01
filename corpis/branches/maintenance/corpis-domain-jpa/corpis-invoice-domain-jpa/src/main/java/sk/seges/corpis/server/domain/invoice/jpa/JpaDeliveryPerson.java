package sk.seges.corpis.server.domain.invoice.jpa;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.validation.Valid;

import sk.seges.corpis.server.domain.customer.jpa.JpaCompanyName;
import sk.seges.corpis.server.domain.customer.jpa.JpaPersonName;
import sk.seges.corpis.server.domain.invoice.server.model.base.DeliveryPersonBase;
import sk.seges.corpis.server.domain.server.model.data.CompanyNameData;
import sk.seges.corpis.server.domain.server.model.data.PersonNameData;

@Embeddable
public class JpaDeliveryPerson extends DeliveryPersonBase {

	private static final long serialVersionUID = 361650212811681144L;

	public static final String TABLE_PREFIX = "delivery_";

	public JpaDeliveryPerson() {
		setCompany(new JpaCompanyName());
		setPerson(new JpaPersonName());
	}

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = CompanyNameData.COMPANY_NAME, column = @Column(name = TABLE_PREFIX + CompanyNameData.COMPANY_NAME)),
			@AttributeOverride(name = CompanyNameData.DEPARTMENT, column = @Column(name = TABLE_PREFIX+ CompanyNameData.DEPARTMENT)) })
	@Valid
	public CompanyNameData getCompany() {
		return super.getCompany();
	}

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = PersonNameData.FIRST_NAME, column = @Column(name = TABLE_PREFIX + PersonNameData.FIRST_NAME)),
			@AttributeOverride(name = PersonNameData.SURNAME, column = @Column(name = TABLE_PREFIX + PersonNameData.SURNAME)) })
	@Valid
	public PersonNameData getPerson() {
		return super.getPerson();
	}

}
