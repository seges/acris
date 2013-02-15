package sk.seges.sesam.core.pap.model.mutable.api;

public interface MutableWildcardType extends MutableTypeVariable {

	public static final String WILDCARD_NAME = "?";

	MutableTypeMirror getExtendsBound();
	void setExtendsBound(MutableTypeMirror bound);
	
	MutableTypeMirror getSuperBound();
	void setSuperBound(MutableTypeMirror bound);
}