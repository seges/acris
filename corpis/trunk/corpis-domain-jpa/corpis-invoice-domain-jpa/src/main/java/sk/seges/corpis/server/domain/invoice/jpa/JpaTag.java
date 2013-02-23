package sk.seges.corpis.server.domain.invoice.jpa;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.invoice.server.model.base.TagBase;
import sk.seges.corpis.server.domain.invoice.server.model.data.TagData;
import sk.seges.corpis.server.domain.invoice.server.model.data.TagNameData;

@Entity
@Table(name = "tag")
@SequenceGenerator(name = JpaTag.SEQ_TAG, sequenceName = "seg_tag", initialValue = 1)
public class JpaTag extends TagBase {

	private static final long serialVersionUID = -8053971923634270766L;

	protected static final String SEQ_TAG = "seqTags";
	
	@Override
	@Id
	@GeneratedValue(generator = SEQ_TAG)
	public Long getId() {
		return super.getId();
	}

	@Override
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REMOVE }, targetEntity = JpaTagName.class)
	public List<TagNameData> getTagNames() {
		return super.getTagNames();
	}
	
	@Override
	@ManyToOne(targetEntity = JpaTag.class)
	public TagData getParent() {
		return super.getParent();
	}
	
	@Override
	@Column
	public Long getPriority() {
		return super.getPriority();
	}
	
	@Override
	@Column(nullable = false)
	public String getWebId() {
		return super.getWebId();
	}
}