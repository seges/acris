package sk.seges.sesam.pap.model.model;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;

public class Field {

	private final String name;
	private final MutableTypeMirror type;
	private MutableTypeMirror castType;
	
	public Field(String name, MutableTypeMirror type) {
		this.name = name;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}

	public MutableTypeMirror getType() {
		return type;
	}
	
	public MutableTypeMirror getCastType() {
		return castType;
	}
	
	public void setCastType(MutableTypeMirror castType) {
		this.castType = castType;
	}
}