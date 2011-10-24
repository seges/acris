package sk.seges.acris.binding.client.samples.mocks;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import sk.seges.acris.binding.client.annotations.BeanWrapper;
import sk.seges.sesam.domain.IDomainObject;
import sk.seges.sesam.model.metadata.annotation.MetaModel;

@MetaModel
@BeanWrapper
public class SimpleBean implements IDomainObject<Long> {
 
	private static final long serialVersionUID = -4200473134297879595L;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		SimpleBean other = (SimpleBean) obj;
		if (email == null) {
			if (other.email != null) return false;
		} else if (!email.equals(other.email)) return false;
		return true;
	}
}