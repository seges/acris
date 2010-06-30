package sk.seges.corpis.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import sk.seges.sesam.domain.IDomainObject;

@Entity
@Table(name = "users")
public class UserTestDO implements IDomainObject<Long> {
	private static final long serialVersionUID = 1163764524617034015L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private String login;
	private String password;
	private String name;
	@ManyToOne(cascade = CascadeType.PERSIST)
	private LocationTestDO birthplace;
	
	@Embedded
	private MailTemplateTestEmbeddable mailTemplate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocationTestDO getBirthplace() {
		return birthplace;
	}

	public void setBirthplace(LocationTestDO birthplace) {
		this.birthplace = birthplace;
	}
	
	public MailTemplateTestEmbeddable getMailTemplate() {
		return mailTemplate;
	}

	public void setMailTemplate(MailTemplateTestEmbeddable mailTemplate) {
		this.mailTemplate = mailTemplate;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", login=" + login + ", name=" + name
				+ ", password=" + password + ", birthplace = "
				+ (birthplace == null ? "n/a" : birthplace) + "]";
	}
}
