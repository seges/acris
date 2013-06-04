package sk.seges.corpis.server.domain.invoice.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.server.model.base.DescriptionBase;

@Entity
@Table(name = "description")
@SequenceGenerator(name = JpaDescription.SEQ_OLEA_DESC, sequenceName = "seq_desc", initialValue = 1)
public class JpaDescription extends DescriptionBase {

	private static final long serialVersionUID = -1671832806412598227L;
	
	protected static final String SEQ_OLEA_DESC = "seqOleaDesc";

	public JpaDescription() {
		super();
	}
	
	public JpaDescription(String language, String value) {
		setLanguage(language);
		setValue(value);
	}

	@Override
	@Id
	@GeneratedValue(generator = SEQ_OLEA_DESC)
	public Long getId() {
		return super.getId();
	}

	@Override
	@Lob
	@Column(columnDefinition = "text", name = "description")
	public String getValue() {
		return super.getValue();
	}

	@Override
	@Column
	public String getLanguage() {
		return super.getLanguage();
	}
}