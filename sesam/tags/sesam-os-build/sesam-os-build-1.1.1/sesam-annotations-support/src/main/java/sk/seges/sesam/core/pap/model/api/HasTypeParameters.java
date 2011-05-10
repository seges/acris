package sk.seges.sesam.core.pap.model.api;

import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;

public interface HasTypeParameters extends MutableType {
	TypeParameter[] getTypeParameters();
	
	String toString(NamedType inputClass, ClassSerializer serializer, boolean typed);
	
	MutableType stripTypeParameters();
}