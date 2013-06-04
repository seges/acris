package sk.seges.sesam.core.pap.model.mutable.api.reference;

import sk.seges.sesam.core.pap.model.mutable.api.MutableReferenceTypeValue;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableExecutableElement;

public interface ExecutableElementReference extends MutableReferenceTypeValue {

	MutableExecutableElement getReference();

}