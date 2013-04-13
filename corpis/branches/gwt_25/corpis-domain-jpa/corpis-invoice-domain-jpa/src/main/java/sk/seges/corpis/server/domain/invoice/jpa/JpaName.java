package sk.seges.corpis.server.domain.invoice.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.server.model.base.NameBase;

@Entity
@Table(name = "name")
@SequenceGenerator(name = JpaName.SEQ_OLEA_NAMES, sequenceName = "seq_name", initialValue = 1)
public class JpaName extends NameBase {

	private static final long serialVersionUID = -5163495413909118691L;

	protected static final String SEQ_OLEA_NAMES = "seqOleaNames";
	private Long id;
	
	public JpaName() {}
	
	public JpaName(String language, String value) {
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

	@Id
	@GeneratedValue(generator = SEQ_OLEA_NAMES)
	public Long getId() {
		return id;
	}
}