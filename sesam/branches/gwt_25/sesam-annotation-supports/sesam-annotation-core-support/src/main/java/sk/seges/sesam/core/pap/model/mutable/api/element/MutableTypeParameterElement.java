package sk.seges.sesam.core.pap.model.mutable.api.element;

import java.util.List;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;

public interface MutableTypeParameterElement extends MutableElementType {

	MutableElementType getGenericElement();

	List<MutableTypeMirror> getBounds();

}