package sk.seges.sesam.core.pap.model.mutable.api;

import sk.seges.sesam.core.pap.model.api.ClassSerializer;

public interface MutableType {
	
	String toString(ClassSerializer serializer);
	String toString(ClassSerializer serializer, boolean typed);

}