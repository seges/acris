package sk.seges.sesam.core.pap.model.mutable.api;

import java.util.List;

import javax.lang.model.element.Modifier;

import sk.seges.sesam.core.pap.model.api.HasAnnotations;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableVariableElement;

public interface MutableExecutableType extends MutableTypeMirror, PrintableType, HasAnnotations {

	List<Modifier> getModifiers();
	MutableExecutableType setModifier(Modifier... modifiers);
	MutableExecutableType addModifier(Modifier... modifiers);

	List<MutableTypeVariable> getTypeVariables();
	MutableExecutableType setTypeVariables(List<MutableTypeVariable> variables);
	
	List<MutableVariableElement> getParameters();
	MutableExecutableType addParameter(MutableVariableElement parameter);
	MutableExecutableType setParameters(List<MutableVariableElement> parameters);
	
	MutableTypeMirror getReturnType();
	MutableExecutableType setReturnType(MutableTypeMirror type);

	MutableExecutableType setSimpleName(String simpleName);
	String getSimpleName();

	List<MutableTypeMirror> getThrownTypes();
	MutableExecutableType setThrownTypes(List<MutableTypeMirror> thrownTypes);
	MutableExecutableType addThrownType(MutableTypeMirror thrownType);	  

	boolean isDefault();
}