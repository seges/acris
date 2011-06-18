package sk.seges.sesam.core.pap.model.api;

import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;

public interface PrintableType {

	String toString(ClassSerializer serializer);

	String toString(NamedType inputClass, ClassSerializer serializer, boolean typed);

}