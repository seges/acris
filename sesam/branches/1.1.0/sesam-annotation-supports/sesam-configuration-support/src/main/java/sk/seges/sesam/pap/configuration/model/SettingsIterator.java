package sk.seges.sesam.pap.configuration.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import sk.seges.sesam.core.configuration.annotation.Settings;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester.AnnotationClassProperty;
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

				SettingsTypeElement settingsTypeElement = new SettingsTypeElement((DeclaredType) context.getNestedElement().asType(), processingEnv);				
				TypeElement settingElement = processingEnv.getElementUtils().getTypeElement(settingsTypeElement.getCanonicalName());
				
				if (settingElement != null && settingElement.getAnnotation(Settings.class) != null) {
					TypeElement configurationClass = AnnotationClassPropertyHarvester.getTypeOfClassProperty(settingElement.getAnnotation(Settings.class), new AnnotationClassProperty<Settings>() {
						@Override
						public Class<?> getClassProperty(Settings annotation) {
							return annotation.configuration();
						}						
					});
					
					if (configurationClass.equals(context.getNestedElement())) {
						nestedClasses.add(context.getNestedOutputName().getCanonicalName());
					}
				}
				
				if (!nestedClasses.contains(context.getNestedOutputName().getCanonicalName())) {
					nestedClasses.add(context.getNestedOutputName().getCanonicalName());
					context.setNestedElementExists(false);
				} else {
					context.setNestedElementExists(true);
				}
				
				printer.print(context);
			}

			return true;
		}
	}
	
	private List<String> nestedClasses = new ArrayList<String>();

	private final List<ExecutableElement> methods;
	private final TypeElement annotationElement;
	private final ProcessingEnvironment processingEnv;
	
	private int index = -1;
	
	public SettingsIterator(TypeElement annotationElement, ProcessingEnvironment processingEnv) {
		this.annotationElement = annotationElement;
		this.processingEnv = processingEnv;
		this.methods = getSortedMethods(annotationElement);
	}

	public SettingsIterator(AnnotationMirror annotationMirror, ProcessingEnvironment processingEnv) {
		TypeElement annotationElement = (TypeElement) annotationMirror.getAnnotationType().asElement();
		this.methods = getSortedMethods(annotationElement);
		this.annotationElement = annotationElement;
		this.processingEnv = processingEnv;
	}

	private List<ExecutableElement> getSortedMethods(TypeElement type) {
		List<ExecutableElement> methods = ElementFilter.methodsIn(type.getEnclosedElements());
		
		List<ExecutableElement> result = new ArrayList<ExecutableElement>();
		
		for (ExecutableElement method: methods) {
			if (method.getAnnotation(Parameter.class) != null) {
				result.add(method);
			}
		}
		
		Collections.sort(result, new Comparator<ExecutableElement>() {

			@Override
			public int compare(ExecutableElement o1, ExecutableElement o2) {
				return o1.getSimpleName().toString().compareTo(o2.getSimpleName().toString());
			}
		});
		
		return result;
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