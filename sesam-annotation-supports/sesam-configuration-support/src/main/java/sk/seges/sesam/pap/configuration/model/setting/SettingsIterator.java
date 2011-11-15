package sk.seges.sesam.pap.configuration.model.setting;

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

import sk.seges.sesam.core.configuration.annotation.Parameter;
import sk.seges.sesam.core.pap.comparator.ExecutableComparator;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.configuration.model.AbstractParameterHandler;
import sk.seges.sesam.pap.configuration.model.AbstractParameterIterator;
import sk.seges.sesam.pap.configuration.model.setting.SettingsIterator.SettingsHandler;
import sk.seges.sesam.pap.configuration.printer.api.AbstractElementPrinter;

public class SettingsIterator extends AbstractParameterIterator<SettingsHandler> {

	protected List<String> nestedClasses = new ArrayList<String>();

	public class SettingsHandler extends AbstractParameterHandler {

		protected ExecutableElement method;

		SettingsHandler(ExecutableElement method) {
			this.method = method;
		}

		@Override
		public boolean handle(AbstractElementPrinter<SettingsContext> printer) {
			
			Parameter parameterAnnotation = method.getAnnotation(Parameter.class);

			if (parameterAnnotation == null) {
				return false;
			}
			
			SettingsContext context = new SettingsContext();
			context.setMethod(method);
			context.setConfigurationElement(annotationElement);
			context.setParameter(parameterAnnotation);

			if (!method.getReturnType().getKind().equals(TypeKind.VOID) && (!method.getReturnType().getKind().equals(TypeKind.DECLARED) ||
					!((DeclaredType)method.getReturnType()).asElement().getKind().equals(ElementKind.ANNOTATION_TYPE))) {
				initializeContext(context);
				printer.print(context);
			} else if (method.getReturnType().getKind().equals(TypeKind.DECLARED) && 
					((DeclaredType)method.getReturnType()).asElement().getKind().equals(ElementKind.ANNOTATION_TYPE)) {
				context.setNestedElement((TypeElement)((DeclaredType)method.getReturnType()).asElement());
				context.setPrefix(parameterAnnotation.name() + ".");
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

	public SettingsIterator(TypeElement annotationElement, MutableProcessingEnvironment processingEnv) {
		super(annotationElement, processingEnv);
	}

	public SettingsIterator(AnnotationMirror annotationMirror, MutableProcessingEnvironment processingEnv) {
		super(annotationMirror, processingEnv);
	}

	@Override
	protected SettingsHandler constructHandler(ExecutableElement method) {
		return new SettingsHandler(method);
	}

	protected List<ExecutableElement> getSortedMethods(TypeElement type) {
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
}