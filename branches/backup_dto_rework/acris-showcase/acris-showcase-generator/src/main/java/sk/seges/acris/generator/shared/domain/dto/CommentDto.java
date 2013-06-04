package sk.seges.acris.generator.shared.domain.dto;

import sk.seges.acris.generator.shared.domain.api.CommentData;
import sk.seges.sesam.domain.IMutableDomainObject;

public class CommentDto implements CommentData, IMutableDomainObject<Long>{

	private static final long serialVersionUID = -2998700033476290251L;

	private String value;
	private Long id;
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long t) {
		this.id = t;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}
}