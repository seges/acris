package sk.seges.sesam.core.pap.model.mutable.utils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.ExecutableType;

import sk.seges.sesam.core.pap.model.InitializableValue;
import sk.seges.sesam.core.pap.model.api.HasAnnotations;
import sk.seges.sesam.core.pap.model.mutable.api.MutableAnnotationMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableExecutableType;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableElementKind;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableElementType;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableExecutableElement;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableTypeParameterElement;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableVariableElement;

class MutableExecutable implements MutableExecutableElement {

	private final AnnotationHolderDelegate annotationHolderDelegate;
	private final ExecutableElement executableElement;
	private final MutableProcessingEnvironment processingEnv;

	private final InitializableValue<MutableExecutableType> type = new InitializableValue<MutableExecutableType>();
	private final InitializableValue<String> simpleName = new InitializableValue<String>();
	
	MutableExecutable(ExecutableElement executableElement, MutableProcessingEnvironment processingEnv) {
		this.executableElement = executableElement;
		this.processingEnv = processingEnv;
		this.annotationHolderDelegate = new AnnotationHolderDelegate(processingEnv, executableElement);
	}

	MutableExecutable(String simpleName, MutableProcessingEnvironment processingEnv) {
		this.executableElement = null;
		this.simpleName.setValue(simpleName);
		this.processingEnv = processingEnv;
		this.annotationHolderDelegate = new AnnotationHolderDelegate(processingEnv);
	}

	@Override
	public MutableExecutableType asType() {
		if (!this.type.isInitialized()) {
			if (this.executableElement != null) {
				this.type.setValue(new MutableMethod(processingEnv, (ExecutableType) this.executableElement.asType()));
			} else {
				this.type.setValue(new MutableMethod(processingEnv, getSimpleName()));
			}
		}
		
		return this.type.getValue();
	}

	@Override
	public MutableElementKind getKind() {
		return MutableElementKind.EXECUTABLE;
	}

	@Override
	public Set<Modifier> getModifiers() {
		return Collections.unmodifiableSet(executableElement.getModifiers());
	}

	@Override
	public String getSimpleName() {
		if (!this.simpleName.isInitialized()) {
			if (this.executableElement != null) {
				this.simpleName.setValue(executableElement.getSimpleName().toString());
			} else {
				throw new RuntimeException("No executable element nor simple name was specified!");
			}
		}
		
		return this.simpleName.getValue();
	}

    public <A extends Annotation> A getAnnotation(final Class<A> annotationType) {
    	return annotationHolderDelegate.getAnnotation(annotationType);
    }

	public HasAnnotations annotateWith(AnnotationMirror annotation) {
		//TODO TODO!!!!!!!
		//dirty();
		annotationHolderDelegate.annotateWith(annotation);
		return this;
	}

	public Set<AnnotationMirror> getAnnotations() {
		return annotationHolderDelegate.getAnnotations();
	}
	
	@Override
	public MutableExecutableElement clone() {
		MutableExecutable result = null;
		
		if (executableElement != null) {
			result = new MutableExecutable(executableElement, processingEnv);
		} else {
			result = new MutableExecutable(getSimpleName(), processingEnv);
		}

		annotationHolderDelegate.clone(result.annotationHolderDelegate);

		if (type.isInitialized()) {
			result.type.setValue(this.type.getValue());
		}

		return result;
	}

	@Override
	public String toString() {
		//TODO
		return getSimpleName();
	}

	@Override
	public MutableElementType getEnclosingElement() {
		//TODO
		throw new RuntimeException("Not supported yet");
	}

	@Override
	public List<MutableElementType> getEnclosedElements() {
		//TODO
		return new ArrayList<MutableElementType>();
	}

	public boolean isVarArgs() {
		//TODO
		return executableElement.isVarArgs();
	}

	@Override
	public List<MutableTypeParameterElement> getTypeParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MutableVariableElement> getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasAnnotations annotateWith(MutableAnnotationMirror mutableAnnotationMirror) {
    	annotationHolderDelegate.annotateWith(mutableAnnotationMirror);
    	return this;
	}

	@Override
	public Set<MutableAnnotationMirror> getMutableAnnotations() {
    	return annotationHolderDelegate.getMutableAnnotations();
	}
}