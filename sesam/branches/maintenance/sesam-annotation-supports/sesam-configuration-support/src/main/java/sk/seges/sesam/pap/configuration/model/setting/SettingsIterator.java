package sk.seges.sesam.pap.configuration.model.setting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.configuration.annotation.Parameter;
import sk.seges.sesam.core.pap.comparator.ExecutableComparator;
import sk.seges.sesam.core.pap.comparator.TypeComparator;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.configuration.model.AbstractParameterHandler;
import sk.seges.sesam.pap.configuration.model.AbstractParameterIterator;
import sk.seges.sesam.pap.configuration.model.parameter.ParametersIterator.AnnotationParameterHandler;
import sk.seges.sesam.pap.configuration.model.parameter.ParametersIterator.MethodParameterHandler;
import sk.seges.sesam.pap.configuration.printer.api.AbstractElementPrinter;

public class SettingsIterator extends AbstractParameterIterator<AbstractParameterHandler> {

	public class MethodSettingsHandler extends MethodParameterHandler {

		public MethodSettingsHandler(ExecutableElement method, TypeElement configurationElement, MutableProcessingEnvironment processingEnv) {
			super(method, configurationElement, processingEnv);
		}

		@Override
		public boolean handle(AbstractElementPrinter<SettingsContext> printer) {

			Parameter parameterAnnotation = method.getAnnotation(Parameter.class);

			if (parameterAnnotation == null) {
				return false;
			}

			SettingsContext context = getContext();
			context.setParameter(parameterAnnotation);
			printer.print(context);
			return true;
		}
	}

	public SettingsIterator(TypeElement annotationElement, ElementKind kind, MutableProcessingEnvironment processingEnv) {
		super(annotationElement, processingEnv, kind);
	}

	public SettingsIterator(AnnotationMirror annotationMirror, ElementKind kind, MutableProcessingEnvironment processingEnv) {
		super(annotationMirror, processingEnv, kind);
	}

	@Override
	protected AbstractParameterHandler constructHandler(Element element) {
		if (element.getKind().equals(ElementKind.METHOD)) {
			return new MethodSettingsHandler((ExecutableElement)element, annotationElement, processingEnv);
		}
		if (element.getKind().equals(ElementKind.ANNOTATION_TYPE)) {
			return new AnnotationParameterHandler((TypeElement)element, annotationElement, processingEnv);
		}
		throw new RuntimeException("Unsupported element kind " + element.getKind());
	}

	protected List<? extends Element> getSortedElements(TypeElement type) {
		if (this.kind.equals(ElementKind.METHOD)) {
			List<ExecutableElement> methods = ElementFilter.methodsIn(type.getEnclosedElements());
			
			List<ExecutableElement> result = new ArrayList<ExecutableElement>();
			
			for (ExecutableElement method: methods) {
				if (method.getAnnotation(Parameter.class) != null) {
					result.add(method);
				}
			}
			
			Collections.sort(result, new ExecutableComparator());
			return result;
		}
		
		if (this.kind.equals(ElementKind.ANNOTATION_TYPE)) {
			List<TypeElement> types = ElementFilter.typesIn(type.getEnclosedElements());
			List<TypeElement> result = new LinkedList<TypeElement>();
			for (TypeElement typeElement: types) {
				if (typeElement.getKind().equals(ElementKind.ANNOTATION_TYPE)) {
					result.add(typeElement);
				}
			}
			Collections.sort(result, new TypeComparator());
			return result;
		}
		
		return new ArrayList<Element>();
	}
}