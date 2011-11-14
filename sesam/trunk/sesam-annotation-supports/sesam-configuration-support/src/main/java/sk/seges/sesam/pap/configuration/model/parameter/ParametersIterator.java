package sk.seges.sesam.pap.configuration.model.parameter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.configuration.model.AbstractParameterHandler;
import sk.seges.sesam.pap.configuration.model.AbstractParameterIterator;
import sk.seges.sesam.pap.configuration.model.parameter.ParametersIterator.ParameterHandler;
import sk.seges.sesam.pap.configuration.model.setting.SettingsContext;
import sk.seges.sesam.pap.configuration.printer.api.AbstractElementPrinter;

public class ParametersIterator extends AbstractParameterIterator<ParameterHandler> {

	protected List<String> nestedClasses = new ArrayList<String>();

	public class ParameterHandler extends AbstractParameterHandler {
		
		protected ExecutableElement method;
		
		protected ParameterHandler(ExecutableElement method) {
			this.method = method;
		}

		@Override
		public boolean handle(AbstractElementPrinter<SettingsContext> printer) {
			
			SettingsContext context = new SettingsContext();
			context.setMethod(method);
			context.setConfigurationElement(annotationElement);

			if (!method.getReturnType().getKind().equals(TypeKind.VOID) && (!method.getReturnType().getKind().equals(TypeKind.DECLARED) ||
					!((DeclaredType)method.getReturnType()).asElement().getKind().equals(ElementKind.ANNOTATION_TYPE))) {
				initializeContext(context);
				printer.print(context);
			} else if (method.getReturnType().getKind().equals(TypeKind.DECLARED) && 
					((DeclaredType)method.getReturnType()).asElement().getKind().equals(ElementKind.ANNOTATION_TYPE)) {
				context.setNestedElement((TypeElement)((DeclaredType)method.getReturnType()).asElement());
				initializeContext(context);

				DeclaredType type = (DeclaredType) context.getNestedElement().asType();
				
				context.setNestedElementExists(!getEnclosingElement(type.asElement()).equals(context.getConfigurationElement()));
				
				if (!nestedClasses.contains(context.getNestedMutableType().getCanonicalName())) {
					nestedClasses.add(context.getNestedMutableType().getCanonicalName());
				} else {
					context.setNestedElementExists(true);
				}
				
				printer.print(context);
			}

			return true;
		}
	}

	public ParametersIterator(TypeElement annotationElement, MutableProcessingEnvironment processingEnv) {
		super(annotationElement, processingEnv);
	}

	public ParametersIterator(AnnotationMirror annotationMirror, MutableProcessingEnvironment processingEnv) {
		super(annotationMirror, processingEnv);
	}

	protected List<ExecutableElement> getSortedMethods(TypeElement type) {
		List<ExecutableElement> methods = ElementFilter.methodsIn(type.getEnclosedElements());
		Collections.sort(methods, new MethodComparator());
		return methods;
	}
	
	protected ParameterHandler constructHandler(ExecutableElement method) {
		return new ParameterHandler(method);
	}
}