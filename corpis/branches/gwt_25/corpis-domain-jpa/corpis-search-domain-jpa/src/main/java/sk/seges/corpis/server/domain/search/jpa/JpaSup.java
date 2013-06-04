package sk.seges.corpis.server.domain.search.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.search.server.model.base.SupBase;
import sk.seges.corpis.server.domain.search.server.model.data.SupData;

@Entity
@Table(name = "sup")
@SequenceGenerator(name = JpaSup.SEQ_SUP, sequenceName=JpaSup.SEQ_SUP, initialValue=1)
public class JpaSup extends SupBase implements SupData {
	private static final long serialVersionUID = -8093324696842872725L;
	
	protected static final String SEQ_SUP = "seq_sups";
	
	@Override
	@Id
	@GeneratedValue(generator = SEQ_SUP)
	public Long getId() {
		return super.getId();
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
	@Column(nullable = false)
	public String getWebId() {
		return super.getWebId();
	}

	@Override
	@ManyToOne(targetEntity = JpaSup.class)
	public JpaSup getParentSup() {
		return (JpaSup) super.getParentSup();
	}
	
	@Override
	@Column
	public String getType() {
		return super.getType();
	}

	@Override
	@Column
	public String getClassType() {
		return super.getClassType();
	}
}
