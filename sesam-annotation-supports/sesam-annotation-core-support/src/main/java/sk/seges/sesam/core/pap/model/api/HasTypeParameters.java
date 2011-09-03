package sk.seges.sesam.core.pap.model.api;

import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;

public interface HasTypeParameters extends ImmutableType {
	TypeParameter[] getTypeParameters();
	
	String toString(ClassSerializer serializer, boolean typed);
	
	ImmutableType stripTypeParameters();
}