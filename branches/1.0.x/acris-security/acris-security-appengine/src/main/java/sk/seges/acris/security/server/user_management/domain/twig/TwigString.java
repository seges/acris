package sk.seges.acris.security.server.user_management.domain.twig;

import sk.seges.sesam.domain.IDomainObject;

import com.vercer.engine.persist.annotation.Key;

public class TwigString implements IDomainObject<Long> {

	private static final long serialVersionUID = 806679460161418170L;

	private @Key
	Long id;

	private String value;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
