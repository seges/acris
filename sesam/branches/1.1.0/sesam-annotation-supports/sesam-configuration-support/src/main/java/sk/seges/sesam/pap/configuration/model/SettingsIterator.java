package sk.seges.sesam.pap.configuration.model;

import java.util.Iterator;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.configuration.annotation.Parameter;
import sk.seges.sesam.pap.configuration.model.SettingsIterator.SettingsHandler;
import sk.seges.sesam.pap.configuration.printer.api.SettingsElementPrinter;

public class SettingsIterator implements Iterator<SettingsHandler> {

	public class SettingsHandler {

		private ExecutableElement method;
		
		SettingsHandler(ExecutableElement method) {
			this.method = method;
		}
		
		public boolean handle(SettingsElementPrinter printer) {
			
			Parameter parameterAnnotation = method.getAnnotation(Parameter.class);

			SettingsContext context = new SettingsContext();
			context.setMethod(method);
			context.setConfigurationElement(annotationElement);

			if (parameterAnnotation != null && !method.getReturnType().getKind().equals(TypeKind.VOID)) {
				context.setParameter(parameterAnnotation);
				initializeContext(context);
				printer.print(context);
			} else if (method.getReturnType().getKind().equals(TypeKind.DECLARED) && 
					((DeclaredType)method.getReturnType()).asElement().getKind().equals(ElementKind.ANNOTATION_TYPE)) {
				context.setNestedElement((TypeElement)((DeclaredType)method.getReturnType()).asElement());
				initializeContext(context);
				printer.print(context);
			}
			
			return true;
		}
	}
	
	private final List<ExecutableElement> methods;
	private final TypeElement annotationElement;
	private final ProcessingEnvironment processingEnv;
	
	private int index = -1;
	
	public SettingsIterator(TypeElement annotationElement, ProcessingEnvironment processingEnv) {
		this.methods = ElementFilter.methodsIn(annotationElement.getEnclosedElements());
		this.annotationElement = annotationElement;
		this.processingEnv = processingEnv;
	}

	public SettingsIterator(AnnotationMirror annotationMirror, ProcessingEnvironment processingEnv) {
		TypeElement annotationElement = (TypeElement) annotationMirror.getAnnotationType().asElement();
		this.methods = ElementFilter.methodsIn(annotationElement.getEnclosedElements());
		this.annotationElement = annotationElement;
		this.processingEnv = processingEnv;
	}

	@Override
	public boolean hasNext() {
		return (index + 1) < methods.size();
	}

	@Override
	public SettingsHandler next() {
		if (index >= methods.size()) {
			return null;
		}
		return new SettingsHandler(methods.get(++index));
	}

	private void initializeContext(SettingsContext context) {
		String fieldName = context.getMethod().getSimpleName().toString();
		context.setFieldName(fieldName);
		
		if (context.getNestedElement() != null) {
			context.setNestedOutputName(new SettingsTypeElement((DeclaredType) context.getNestedElement().asType(), processingEnv));
		}
	}

	@Override
	public void remove() {
		throw new RuntimeException("Unsupported operation");
	}
}