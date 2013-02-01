package sk.seges.sesam.core.pap.model;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;

public class ConstructorParameter {

	private String name;
	private final MutableTypeMirror type;
	
	public ConstructorParameter(MutableTypeMirror type, String name) {
		this.type = type;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public ConstructorParameter setName(String name) {
		this.name = name;
		return this;
	}

	public MutableTypeMirror getType() {
		return type;
	}
}