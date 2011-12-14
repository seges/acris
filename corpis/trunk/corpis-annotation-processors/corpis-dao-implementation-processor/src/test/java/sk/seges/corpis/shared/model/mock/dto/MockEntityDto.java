package sk.seges.corpis.shared.model.mock.dto;

import sk.seges.corpis.shared.model.mock.api.MockEntityData;

public class MockEntityDto implements MockEntityData {

	private static final long serialVersionUID = 7136106580590640853L;

	private Long id;
	private String name;
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
}