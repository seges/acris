package sk.seges.corpis.server.domain.invoice.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.invoice.server.model.base.TagNameBase;
import sk.seges.sesam.domain.IDomainObject;

@Entity
@Table(name = "tag_name")
@SequenceGenerator(name = JpaTagName.SEQ_OLEA_TAG_NAMES, sequenceName = "seq_tag_name", initialValue = 1)
public class JpaTagName extends TagNameBase implements IDomainObject<Long> {

	private static final long serialVersionUID = 5807542710037902196L;

	protected static final String SEQ_OLEA_TAG_NAMES = "seqTagNames";

	@Id
	@GeneratedValue(generator = SEQ_OLEA_TAG_NAMES)
	@Override
	public Long getId() {
		return super.getId();
	}
	
	@Override
	@Column
	public String getLanguage() {
		return super.getLanguage();
	}
	
	@Override
	@Column
	public String getValue() {
		return super.getValue();
	}
}