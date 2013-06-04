package sk.seges.corpis.server.domain.invoice.jpa;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import sk.seges.corpis.server.domain.server.model.base.NameBase;

@MappedSuperclass
public class JpaEmbeddedName extends NameBase {

	private static final long serialVersionUID = -5163495413909118691L;

	public JpaEmbeddedName() {}
	
	public JpaEmbeddedName(String language, String value) {
		setLanguage(language);
		setValue(value);
	}
	
	@Column
	@Override
	public String getLanguage() {
		return super.getLanguage();
	}

	@Column
	@Override
	public String getValue() {
		return super.getValue();
	}
}