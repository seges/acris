package sk.seges.sesam.core.pap.model;

import sk.seges.sesam.core.pap.model.api.NamedType;

public class ParameterElement {

	private NamedType type;
	private String name;
	private boolean isConverter;
	
	public ParameterElement(NamedType type, String name, boolean isConverter) {
		this.type = type;
		this.name = name;
		this.isConverter = isConverter;
	}

	public NamedType getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isConverter() {
		return isConverter;
	}
}