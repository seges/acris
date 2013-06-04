package sk.seges.acris.mvp.server.model.twig;

import sk.seges.acris.mvp.shared.model.api.GroupData;

import com.vercer.engine.persist.annotation.Key;

public class TwigGroup implements GroupData {

	private static final long serialVersionUID = -8914268534814761557L;

	@Key
	private Long id;

	private String description;

	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}