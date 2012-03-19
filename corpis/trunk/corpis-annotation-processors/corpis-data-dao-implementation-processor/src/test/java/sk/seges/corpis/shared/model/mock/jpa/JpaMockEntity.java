package sk.seges.corpis.shared.model.mock.jpa;

import javax.persistence.Column;
import javax.persistence.Id;

import sk.seges.corpis.shared.model.mock.dto.MockEntityBase;

public class JpaMockEntity extends MockEntityBase {

	private static final long serialVersionUID = 3379482381928401916L;

	@Override
	@Id
	public Long getId() {
		return super.getId();
	}
	
	@Override
	@Column
	public String getName() {
		return super.getName();
	}
}