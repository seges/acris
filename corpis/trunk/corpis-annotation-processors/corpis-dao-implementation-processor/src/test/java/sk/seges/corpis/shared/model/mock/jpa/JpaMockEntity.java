package sk.seges.corpis.shared.model.mock.jpa;

import javax.persistence.Column;
import javax.persistence.Id;

import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject;
import sk.seges.corpis.core.shared.annotation.dao.DataAccessObject.Provider;
import sk.seges.corpis.shared.model.mock.dto.MockEntityDto;

@DataAccessObject(provider = Provider.HIBERNATE)
public class JpaMockEntity extends MockEntityDto {

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