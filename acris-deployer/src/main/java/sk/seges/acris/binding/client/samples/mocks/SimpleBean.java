package sk.seges.acris.binding.client.samples.mocks;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import sk.seges.acris.binding.client.annotations.BeanWrapper;
import sk.seges.sesam.domain.IDomainObject;

@BeanWrapper
public class SimpleBean implements IDomainObject<Long> {

	private static final long serialVersionUID = -4200473134297879595L;

	public static final String NAME_ATTRIBUTE = "name";
	public static final String EMAIL_ATTRIBUTE = "email";
	public static final String COMPANY_ATTRIBUTE = "company";
	public static final String DATE_ATTRIBUTE = "date";

	
	private Long id;

	@NotNull
	@Size(min = 5)
	private String name;
	@NotNull(groups = ContactCheck.class)
	private String email;
	@Valid
	private Company company;
	@Future
	private Date date;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
