package sk.seges.sesam.pap.model.model.api;

import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;

public interface ElementHolderTypeConverter {

	TypeMirror getIterableDtoType(MutableTypeMirror collectionType);

}