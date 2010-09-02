package sk.seges.acris.security.server.core.user_management.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import sk.seges.sesam.domain.IDomainObject;

@Entity
public class JpaStringEntity implements IDomainObject<Long> {

	private static final long serialVersionUID = 4215390700888836887L;

	@Column
	private String value;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	public JpaStringEntity() {
	}

	public JpaStringEntity(String value) {
		this.value = value;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getId() {
		return id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}