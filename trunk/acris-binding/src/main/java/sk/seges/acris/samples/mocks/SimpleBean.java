package sk.seges.acris.samples.mocks;

import sk.seges.sesam.domain.IDomainObject;

public class SimpleBean implements IDomainObject<Long> {

	private static final long serialVersionUID = -4200473134297879595L;

	public static final String NAME_ATTRIBUTE = "name";
	public static final String EMAIL_ATTRIBUTE = "email";
	public static final String COMPANY_ATTRIBUTE = "company";

	private Long id;

	private String name;
	private String email;
	private Company company;

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
}
