package sk.seges.sesam.core.pap.model.mutable.api.element;

import java.util.List;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;

public interface MutableTypeParameterElement extends MutableElement {

	MutableElement getGenericElement();

	List<MutableTypeMirror> getBounds();

}