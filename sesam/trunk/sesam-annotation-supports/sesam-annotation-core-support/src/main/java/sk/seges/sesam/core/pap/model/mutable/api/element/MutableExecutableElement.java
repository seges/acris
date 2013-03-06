package sk.seges.sesam.core.pap.model.mutable.api.element;

import java.util.List;

import sk.seges.sesam.core.pap.model.api.HasAnnotations;
import sk.seges.sesam.core.pap.model.mutable.api.MutableExecutableType;

public interface MutableExecutableElement extends MutableElementType, HasAnnotations {

	List<MutableTypeParameterElement> getTypeParameters();
	
	List<MutableVariableElement> getParameters();

	boolean isVarArgs();

	MutableExecutableType asType();
}