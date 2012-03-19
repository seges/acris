package sk.seges.sesam.core.pap.model.mutable.api;

public interface MutableReferenceType extends MutableType {
	
	MutableTypeValue getReference();
	
	void setName(String name);
	
}