package sk.seges.sesam.pap.configuration.model;

import java.util.Iterator;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.configuration.model.parameter.ParameterContext;
import sk.seges.sesam.pap.configuration.model.setting.SettingsTypeElement;

public abstract class AbstractParameterIterator<T extends AbstractParameterHandler> implements Iterator<T> {

	protected abstract T constructHandler(ExecutableElement method);

	protected final List<ExecutableElement> methods;
	protected final TypeElement annotationElement;
	protected final MutableProcessingEnvironment processingEnv;
	
	private int index = -1;
	
	protected AbstractParameterIterator(TypeElement annotationElement, MutableProcessingEnvironment processingEnv) {
		this.annotationElement = annotationElement;
		this.processingEnv = processingEnv;
		this.methods = getSortedMethods(annotationElement);
	}

	protected AbstractParameterIterator(AnnotationMirror annotationMirror, MutableProcessingEnvironment processingEnv) {
		TypeElement annotationElement = (TypeElement) annotationMirror.getAnnotationType().asElement();
		this.methods = getSortedMethods(annotationElement);
		this.annotationElement = annotationElement;
		this.processingEnv = processingEnv;
	}

	protected abstract List<ExecutableElement> getSortedMethods(TypeElement type);
	
	@Override
	public boolean hasNext() {
		return (index + 1) < methods.size();
	}

	@Override
	public T next() {
		if (index >= methods.size()) {
			return null;
		}
		return constructHandler(methods.get(++index));
	}

	protected void initializeContext(ParameterContext context) {
		String fieldName = context.getMethod().getSimpleName().toString();
		context.setFieldName(fieldName);
		
		if (context.getNestedElement() != null) {
			context.setNestedMutableType(new SettingsTypeElement((DeclaredType) context.getNestedElement().asType(), processingEnv));
		}
	}

	@Override
	public void remove() {
		throw new RuntimeException("Unsupported operation");
	}
}