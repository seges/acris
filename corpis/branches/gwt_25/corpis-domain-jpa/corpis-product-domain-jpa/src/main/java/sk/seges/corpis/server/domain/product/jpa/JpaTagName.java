package sk.seges.corpis.server.domain.product.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.invoice.jpa.JpaName;
import sk.seges.corpis.server.domain.product.server.model.data.TagNameData;

@Entity
@Table(name = "tag_name")
@SequenceGenerator(name = JpaTagName.SEQ_OLEA_TAG_NAMES, sequenceName = "seq_tag_name", initialValue = 1)
public class JpaTagName extends JpaName implements TagNameData {

	private static final long serialVersionUID = 5807542710037902196L;

	protected static final String SEQ_OLEA_TAG_NAMES = "seqTagNames";

	@Id
	@GeneratedValue(generator = SEQ_OLEA_TAG_NAMES)
	@Override
	public Long getId() {
		return super.getId();
	}	
}