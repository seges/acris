package sk.seges.sesam.core.pap.model.mutable.api;

import java.util.List;

import javax.lang.model.element.Modifier;

public interface MutableReferenceType extends MutableType {
	
	List<Modifier> getModifiers();

	MutableTypeValue getReference();
	
	void setName(String name);
	
	String getName();

	boolean isInline();
}