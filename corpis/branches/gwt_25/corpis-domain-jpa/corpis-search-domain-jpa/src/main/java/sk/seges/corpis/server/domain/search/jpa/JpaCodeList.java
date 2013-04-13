package sk.seges.corpis.server.domain.search.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import sk.seges.corpis.server.domain.search.server.model.base.CodeListBase;
import sk.seges.corpis.server.domain.search.server.model.data.CodeListData;


@Entity
@Table(name = "code_list")
@SequenceGenerator(name = "code_list_id_seq", sequenceName = "code_list_id_seq", initialValue = 1)
public class JpaCodeList extends CodeListBase implements CodeListData {
	private static final long serialVersionUID = 5623610287643265219L;

	@Override
	@Id
	@GeneratedValue(generator = "code_list_id_seq")
	public Long getId() {
		return super.getId();
	}
	
	@Override
	@Column(nullable = false, length = 1024)
	public String getNames() {
		return super.getNames();
	}
	
	@Override
	@Column(nullable = false, length = 256)
	public String getWebId() {
		return super.getWebId();
	}
	
	@Override
	@Column(nullable = false)
	public int getPosition() {
		return super.getPosition();
	}
	
	@Override
	@Column(nullable = false, length = 256)
	public String getType() {
		return super.getType();
	}
}
