package sk.seges.corpis.server.domain.search.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import sk.seges.corpis.server.domain.search.server.model.base.SupIndexBase;
import sk.seges.corpis.server.domain.search.server.model.data.SupIndexData;

@Entity
@Table(name = "sup_index")
@SequenceGenerator(name = JpaSupIndex.SEQ_SUP_INDEX, sequenceName = JpaSupIndex.SEQ_SUP_INDEX, initialValue = 1)
public class JpaSupIndex extends SupIndexBase implements SupIndexData{
	private static final long serialVersionUID = 5527998192109816664L;
	
	protected static final String SEQ_SUP_INDEX = "seq_sup_index";
	
	@Override
	@Id
	@GeneratedValue(generator = JpaSupIndex.SEQ_SUP_INDEX)
	public Long getId() {
		return super.getId();
	}

	@Override
	@ManyToOne(targetEntity = JpaSup.class)
	public JpaSup getSup() {
		return (JpaSup) super.getSup();
	}

	@Override
	@Column
	public int getPosition() {
		return super.getPosition();
	}

	/**
	 * localized names: {"locale":"localized name","locale_2":"localized_name"}
	 */
	@Override
	@Column
	public String getNames() {
		return super.getNames();
	}

	@Override
	@Transient
	public String getWebId() {
		return getSup().getWebId();
	}

	@Override
	@Transient
	public String getType() {
		return getSup().getType();
	}
}
