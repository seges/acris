package sk.seges.acris.mvp.shared.model.dto;

import sk.seges.acris.mvp.shared.model.api.GroupData;

public class GroupDTO implements GroupData {

	private static final long serialVersionUID = 6783675917228292764L;

	private Long id;
	private String name;
	private String description;

	@Override
	public void setId(Long id) {
		this.id = id;
	}

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

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}
}