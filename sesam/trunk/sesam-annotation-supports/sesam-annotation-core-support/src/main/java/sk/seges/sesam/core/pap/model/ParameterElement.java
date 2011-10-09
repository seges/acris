package sk.seges.sesam.core.pap.model;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;

public class ParameterElement {

	private MutableTypeMirror type;
	private String name;
	private boolean isConverter;

	public ParameterElement(MutableTypeMirror type, String name, boolean isConverter) {
		this.type = type;
		this.name = name;
		this.isConverter = isConverter;
	}

	public MutableTypeMirror getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public boolean isConverter() {
		return isConverter;
	}
}