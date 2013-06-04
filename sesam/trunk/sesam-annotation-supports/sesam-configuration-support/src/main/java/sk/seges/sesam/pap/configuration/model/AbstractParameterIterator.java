package sk.seges.sesam.pap.configuration.model;

import java.util.Iterator;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public abstract class AbstractParameterIterator<T extends AbstractParameterHandler> implements Iterator<T> {

	protected abstract T constructHandler(Element element);

	protected final List<? extends Element> elements;
	protected final TypeElement annotationElement;
	protected final MutableProcessingEnvironment processingEnv;
	protected final ElementKind kind;
	
	private int index = -1;
	
	protected AbstractParameterIterator(TypeElement annotationElement, MutableProcessingEnvironment processingEnv, ElementKind kind) {
		this.kind = kind;
		this.annotationElement = annotationElement;
		this.processingEnv = processingEnv;
		this.elements = getSortedElements(annotationElement);
	}

	protected AbstractParameterIterator(AnnotationMirror annotationMirror, MutableProcessingEnvironment processingEnv, ElementKind kind) {
		TypeElement annotationElement = (TypeElement) annotationMirror.getAnnotationType().asElement();
		this.kind = kind;
		this.elements = getSortedElements(annotationElement);
		this.annotationElement = annotationElement;
		this.processingEnv = processingEnv;
	}

	protected abstract List<? extends Element> getSortedElements(TypeElement type);
	
	@Override
	public boolean hasNext() {
		return (index + 1) < elements.size();
	}

	@Override
	public T next() {
		if (index >= elements.size()) {
			return null;
		}
		return constructHandler(elements.get(++index));
	}

	@Override
	public void remove() {
		throw new RuntimeException("Unsupported operation");
	}
}