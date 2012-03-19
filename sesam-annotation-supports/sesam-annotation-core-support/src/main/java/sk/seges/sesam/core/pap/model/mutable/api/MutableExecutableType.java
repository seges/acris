package sk.seges.sesam.core.pap.model.mutable.api;

import java.util.List;

public interface MutableExecutableType extends MutableTypeMirror {

	  List<MutableTypeVariable> getTypeVariables();
	  MutableExecutableType setTypeVariables(List<MutableTypeVariable> variables);

	  List<MutableTypeMirror> getParameterTypes();
	  MutableExecutableType setParameterTypes(List<MutableTypeMirror> variables);

	  MutableTypeMirror getReturnType();
	  MutableExecutableType setReturnType(MutableTypeMirror type);
	  
	  MutableExecutableType setSimpleName(String simpleName);
	  String getSimpleName();
	  
	  List<MutableTypeMirror> getThrownTypes();
	  MutableExecutableType setThrownTypes(List<MutableTypeMirror> thrownTypes);
	  MutableExecutableType addThrownType(MutableTypeMirror thrownType);	  
}