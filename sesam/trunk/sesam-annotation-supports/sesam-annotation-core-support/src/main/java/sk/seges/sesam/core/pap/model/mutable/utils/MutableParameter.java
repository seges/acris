package sk.seges.sesam.core.pap.model.mutable.utils;

import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableElementKind;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableVariableElement;

class MutableParameter extends MutableHasAnnotationsElement implements MutableVariableElement {

	public MutableParameter(MutableElementKind kind, MutableTypeMirror type, String name, MutableProcessingEnvironment processingEnv) {
		super(kind, type, name, processingEnv);
	}
	
}