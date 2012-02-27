package sk.seges.sesam.core.pap.model.mutable.utils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableElement;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableElementKind;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableExecutableElement;
import sk.seges.sesam.core.pap.model.mutable.api.value.MutableAnnotationValue;

class MutableExecutable implements MutableExecutableElement {

	private final AnnotationHolderDelegate annotationHolderDelegate;
	private final ExecutableElement executableElement;
	private final MutableProcessingEnvironment processingEnv;
	
	private MutableTypeMirror returnType;
	private boolean returnTypeInitialized = false;
	
	MutableExecutable(ExecutableElement executableElement, MutableProcessingEnvironment processingEnv) {
		this.executableElement = executableElement;
		this.processingEnv = processingEnv;
		this.annotationHolderDelegate = new AnnotationHolderDelegate(processingEnv, executableElement);
	}
	
	@Override
	public MutableTypeMirror asType() {
		return new MutableAnnotated(processingEnv, executableElement.asType()) {
			
			@Override
			public String toString(ClassSerializer serializer) {
				return MutableExecutable.this.toString();
			}

			@Override
			public String toString(ClassSerializer serializer, boolean typed) {
				return MutableExecutable.this.toString();
			}

			@Override
			public MutableTypeKind getKind() {
				return MutableTypeKind.METHOD;
			}			
		};
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
	public String toString() {
		//TODO
		return executableElement.getSimpleName().toString();
	}
	
    public <A extends Annotation> A getAnnotation(final Class<A> annotationType) {
    	return annotationHolderDelegate.getAnnotation(annotationType);
    }

	public void annotateWith(AnnotationMirror annotation) {
		//TODO TODO!!!!!!!
		//dirty();
		annotationHolderDelegate.annotateWith(annotation);
	}

	public Set<AnnotationMirror> getAnnotations() {
		return annotationHolderDelegate.getAnnotations();
	}

	@Override
	public MutableExecutableElement clone() {
		MutableExecutable result = new MutableExecutable(executableElement, processingEnv);
		
		annotationHolderDelegate.clone(result.annotationHolderDelegate);

		if (returnTypeInitialized) {
			result.setReturnType(returnType);
		}
		
		return result;
	}

	@Override
	public String getSimpleName() {
		return executableElement.getSimpleName().toString();
	}

	@Override
	public MutableElement getEnclosingElement() {
		throw new RuntimeException("Not supported yet");
	}

	@Override
	public List<MutableElement> getEnclosedElements() {
		return new ArrayList<MutableElement>();
	}

	@Override
	public MutableTypeMirror getReturnType() {
		if (!this.returnTypeInitialized) {
			this.returnType = processingEnv.getTypeUtils().toMutableType(executableElement.getReturnType());
			this.returnTypeInitialized = true;
		}
		return this.returnType;
	}

	@Override
	public MutableExecutableElement setReturnType(MutableTypeMirror type) {
		this.returnType = type;
		this.returnTypeInitialized = true;
		return this;
	}

	public boolean isVarArgs() {
		return executableElement.isVarArgs();
	}

	@Override
	public MutableAnnotationValue getDefaultValue() {
		//TODO
		return null;
	}
}