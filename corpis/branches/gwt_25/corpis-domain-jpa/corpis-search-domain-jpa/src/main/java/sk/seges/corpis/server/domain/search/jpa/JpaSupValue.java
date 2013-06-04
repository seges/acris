package sk.seges.corpis.server.domain.search.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.search.server.model.base.SupValueBase;
import sk.seges.corpis.server.domain.search.server.model.data.SupValueData;

@Entity
@Table(name = "sup_value")
@SequenceGenerator(name = JpaSupValue.SEQ_SUP_VALUE, sequenceName = JpaSupValue.SEQ_SUP_VALUE, initialValue = 1)
public class JpaSupValue extends SupValueBase implements SupValueData {
	private static final long serialVersionUID = -2665880519695805678L;
	
	protected static final String SEQ_SUP_VALUE = "seq_sup_value";

	@Override
	@Id
	@GeneratedValue(generator=SEQ_SUP_VALUE)
	public Long getId() {
		return super.getId();
	}

	@Override
	@ManyToOne(targetEntity = JpaSup.class)
	public JpaSup getSup() {
		return (JpaSup)super.getSup();
	}
	
	@Override
	@Column
	public String getLocale() {
		return super.getLocale();
	}

	@Override
	@Column(length = 10000)
	public String getSValue() {
		return super.getSValue();
	}

	@Override
	@ManyToOne(targetEntity = JpaSupIndex.class)
	public JpaSupIndex getSupIndex() {
		return (JpaSupIndex) super.getSupIndex();
	}
}
