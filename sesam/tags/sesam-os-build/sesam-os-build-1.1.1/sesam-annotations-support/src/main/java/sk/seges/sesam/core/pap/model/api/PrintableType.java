package sk.seges.sesam.core.pap.model.api;

import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;

public interface PrintableType {

//	String getCanonicalName(NamedType outputName, NamedType type, boolean typed);
//
//	String getSimpleName(NamedType outputName, NamedType type, boolean typed);
//
//	String getQualifiedName(InputClass outputName, NamedType type, boolean typed);
//
//	String toString(TypeParameter typeParameter, ClassSerializer serializer);

	String toString(ClassSerializer serializer);

	String toString(NamedType inputClass, ClassSerializer serializer, boolean typed);

}