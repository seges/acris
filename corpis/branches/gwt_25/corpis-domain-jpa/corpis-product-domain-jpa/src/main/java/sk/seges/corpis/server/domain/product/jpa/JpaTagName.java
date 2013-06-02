package sk.seges.corpis.server.domain.product.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.invoice.jpa.JpaEmbeddedName;
import sk.seges.corpis.server.domain.product.server.model.data.TagNameData;
import sk.seges.sesam.domain.IMutableDomainObject;

@Entity
@Table(name = "tag_name")
@SequenceGenerator(name = JpaTagName.SEQ_OLEA_TAG_NAMES, sequenceName = "seq_tag_name", initialValue = 1)
public class JpaTagName extends JpaEmbeddedName implements TagNameData, IMutableDomainObject<Long> {

	private static final long serialVersionUID = 5807542710037902196L;

	protected static final String SEQ_OLEA_TAG_NAMES = "seqTagNames";

	private Long id;
	
	public JpaTagName() {}
	
	public JpaTagName(String language, String tagName) {
		setLanguage(language);
		setValue(tagName);
	}
	
	@Id
	@GeneratedValue(generator = SEQ_OLEA_TAG_NAMES)
	@Override
	public Long getId() {
		return id;
	}	
	
	public void setId(Long id) {
		this.id = id;
	}
}