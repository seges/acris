package sk.seges.corpis.server.domain.invoice.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import sk.seges.sesam.domain.IMutableDomainObject;

@Entity
@Table(name = "name")
@SequenceGenerator(name = JpaName.SEQ_OLEA_NAMES, sequenceName = "seq_name", initialValue = 1)
public class JpaName extends JpaEmbeddedName implements IMutableDomainObject<Long >{

	private static final long serialVersionUID = -5163495413909118691L;

	protected static final String SEQ_OLEA_NAMES = "seqOleaNames";
	
	private Long id;
	
	public JpaName() {}
	
	public JpaName(String language, String value) {
		setLanguage(language);
		setValue(value);
	}
	
	@Id
	@GeneratedValue(generator = SEQ_OLEA_NAMES)
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}
}