package sk.seges.sesam.core.pap.model.mutable.api.element;

import sk.seges.sesam.core.pap.model.api.HasAnnotations;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.value.MutableAnnotationValue;

public interface MutableExecutableElement extends MutableElement, HasAnnotations {
	
//	  List<MutableTypeMirror> getTypeParameters();
	  
	  MutableTypeMirror getReturnType();
	  MutableExecutableElement setReturnType(MutableTypeMirror type);
	  
//	  List<MutableTypeMirror> getParameters();
	  
	  boolean isVarArgs();
	  
//	  List<MutableTypeMirror> getThrownTypes();
	  
	  MutableAnnotationValue getDefaultValue();
}