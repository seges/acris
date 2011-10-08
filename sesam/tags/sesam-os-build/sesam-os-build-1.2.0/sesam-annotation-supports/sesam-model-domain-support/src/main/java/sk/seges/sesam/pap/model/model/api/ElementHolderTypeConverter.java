package sk.seges.sesam.pap.model.model.api;

import javax.lang.model.type.TypeMirror;

public interface ElementHolderTypeConverter {

	TypeMirror getIterableDtoType(TypeMirror collectionType);

}