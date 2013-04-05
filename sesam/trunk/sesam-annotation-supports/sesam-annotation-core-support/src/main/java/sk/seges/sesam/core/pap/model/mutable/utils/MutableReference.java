package sk.seges.sesam.core.pap.model.mutable.utils;

import java.util.Collections;
import java.util.List;

import javax.lang.model.element.Modifier;

import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableReferenceType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeValue;

class MutableReference implements MutableReferenceType {

	private String name;
	private boolean inline; 
	private List<Modifier> modifiers;
	private final MutableTypeValue reference;
	
	MutableReference(MutableTypeValue reference, String name, boolean inline, List<Modifier> modifiers) {
		this.name = name;
		this.reference = reference;
		this.inline = inline;
		this.modifiers = modifiers;
	}
	
	@Override
	public String toString() {
		return name;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String toString(ClassSerializer serializer) {
		return toString(serializer, true);
	}

	@Override
	public String toString(ClassSerializer serializer, boolean typed) {
		return name;
	}

	@Override
	public MutableTypeValue getReference() {
		return reference;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean isInline() {
		return inline;
	}
	
	@Override
	public List<Modifier> getModifiers() {
		return Collections.unmodifiableList(modifiers);
	}
}