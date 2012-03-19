package sk.seges.sesam.pap.configuration.model.parameter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.pap.comparator.ExecutableComparator;
import sk.seges.sesam.core.pap.comparator.TypeComparator;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.configuration.model.AbstractParameterHandler;
import sk.seges.sesam.pap.configuration.model.AbstractParameterIterator;
import sk.seges.sesam.pap.configuration.model.setting.SettingsContext;
import sk.seges.sesam.pap.configuration.model.setting.SettingsTypeElement;
import sk.seges.sesam.pap.configuration.printer.api.AbstractElementPrinter;

public class ParametersIterator extends AbstractParameterIterator<AbstractParameterHandler> {

	public static class AnnotationParameterHandler extends AbstractParameterHandler {

		protected final TypeElement annotation;
		protected final TypeElement configurationElement;
		protected final MutableProcessingEnvironment processingEnv;
		
		public AnnotationParameterHandler(TypeElement annotation, TypeElement configurationElement, MutableProcessingEnvironment processingEnv) {
			this.annotation = annotation;
			this.configurationElement = configurationElement;
			this.processingEnv = processingEnv;
		}

		@Override
		public boolean handle(AbstractElementPrinter<SettingsContext> printer) {
			SettingsContext context = new SettingsContext();
			context.setConfigurationElement(configurationElement);
			context.setNestedElement(annotation);
			initializeContext(context);

			printer.print(context);

			return true;
		}

		protected void initializeContext(ParameterContext context) {
			String fieldName = context.getNestedElement().getSimpleName().toString();

			context.setFieldName(fieldName);
			context.setNestedMutableType(new SettingsTypeElement((DeclaredType) context.getNestedElement().asType(), processingEnv));
		}
	}
	
	public static class MethodParameterHandler extends AbstractParameterHandler {

		protected final TypeElement configurationElement;
		protected final MutableProcessingEnvironment processingEnv;
		
		protected ExecutableElement method;

		public MethodParameterHandler(ExecutableElement method, TypeElement configurationElement, MutableProcessingEnvironment processingEnv) {
			this.method = method;
			this.configurationElement = configurationElement;
			this.processingEnv = processingEnv;
		}

		@Override
		public boolean handle(AbstractElementPrinter<SettingsContext> printer) {
			printer.print(getContext());
			return true;
		}

		protected SettingsContext getContext() {
			SettingsContext context = new SettingsContext();
			context.setMethod(method);
			context.setConfigurationElement(configurationElement);

			if (!method.getReturnType().getKind().equals(TypeKind.VOID) && (!method.getReturnType().getKind().equals(TypeKind.DECLARED) ||
					!((DeclaredType)method.getReturnType()).asElement().getKind().equals(ElementKind.ANNOTATION_TYPE))) {
				initializeContext(context);
			} else if (method.getReturnType().getKind().equals(TypeKind.DECLARED) && 
					((DeclaredType)method.getReturnType()).asElement().getKind().equals(ElementKind.ANNOTATION_TYPE)) {
				context.setNestedElement((TypeElement)((DeclaredType)method.getReturnType()).asElement());
				initializeContext(context);
			}
			
			return context;
		}
		
		protected void initializeContext(ParameterContext context) {
			String fieldName = context.getMethod().getSimpleName().toString();
			context.setFieldName(fieldName);
			
			if (context.getNestedElement() != null) {
				context.setNestedMutableType(new SettingsTypeElement((DeclaredType) context.getNestedElement().asType(), processingEnv));
			}
		}
	}

	public ParametersIterator(TypeElement annotationElement, ElementKind kind, MutableProcessingEnvironment processingEnv) {
		super(annotationElement, processingEnv, kind);
	}

	public ParametersIterator(AnnotationMirror annotationMirror, ElementKind kind, MutableProcessingEnvironment processingEnv) {
		super(annotationMirror, processingEnv, kind);
	}

	protected List<? extends Element> getSortedElements(TypeElement type) {
		if (this.kind.equals(ElementKind.METHOD)) {
			List<ExecutableElement> methods = ElementFilter.methodsIn(type.getEnclosedElements());
			Collections.sort(methods, new ExecutableComparator());
			return methods;
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
	
	@Override
	protected AbstractParameterHandler constructHandler(Element element) {
		if (element.getKind().equals(ElementKind.METHOD)) {
			return new MethodParameterHandler((ExecutableElement)element, annotationElement, processingEnv);
		}
		if (element.getKind().equals(ElementKind.ANNOTATION_TYPE)) {
			return new AnnotationParameterHandler((TypeElement)element, annotationElement, processingEnv);
		}
		throw new RuntimeException("Unsupported element kind " + element.getKind());
	}
}