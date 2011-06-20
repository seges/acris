package sk.seges.acris.theme.pap;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;

import sk.seges.acris.theme.client.annotation.ThemeSupport;
import sk.seges.acris.theme.pap.specific.AbstractComponentSpecificProcessor.Statement;
import sk.seges.acris.theme.pap.specific.ComponentSpecificProcessor;
import sk.seges.acris.theme.pap.specific.ThemeCheckBoxProcessor;
import sk.seges.acris.theme.pap.specific.ThemeImageCheckBoxProcessor;
import sk.seges.acris.theme.pap.util.AnnotationClassPropertyHarvester;
import sk.seges.acris.theme.pap.util.AnnotationClassPropertyHarvester.AnnotationClassProperty;
import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.model.api.MutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ThemeComponentPanelProcessor extends AbstractConfigurableProcessor {

	private Set<ComponentSpecificProcessor> specificProcessors = new HashSet<ComponentSpecificProcessor>();
	
	protected ElementKind getElementKind() {
		return ElementKind.CLASS;
	}
	
	
	public ThemeComponentPanelProcessor() {
		specificProcessors.add(new ThemeImageCheckBoxProcessor());
		specificProcessors.add(new ThemeCheckBoxProcessor());
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> annotations = new HashSet<String>();
		annotations.add(ThemeSupport.class.getCanonicalName());
		return annotations;
	}

	@Override
	public synchronized void init(ProcessingEnvironment pe) {
		super.init(pe);
		for (ComponentSpecificProcessor specificProcessor: specificProcessors) {
			specificProcessor.init(pe);
		}
	}
	
	@Override
	protected Type[] getImports(TypeElement typeElement) {
		Type[] result = new Type[] {
		};
		
		List<Type> importTypes = new ArrayList<Type>();
		
		for (ComponentSpecificProcessor specificProcessor: specificProcessors) {
			TypeElement componentClass = getComponentType(typeElement);
			if (specificProcessor.supports(componentClass)) {
				addUnique(importTypes, specificProcessor.getImports());
			}
		}
		addUnique(importTypes, result);
		return importTypes.toArray(new Type[] {});
	}
	
	public static final MutableType getOutputClass(MutableType mutableType) {
		return mutableType.addClassSufix("Panel");
	}

	@Override
	protected Type[] getConfigurationTypes(DefaultConfigurationType type, TypeElement typeElement) {

		switch (type) {
			case OUTPUT_SUPERCLASS:		
				return new Type[] { getNameTypes().toType(getComponentType(typeElement))};
		}
		
		return super.getConfigurationTypes(type, typeElement);
	}

	@Override
	protected NamedType[] getTargetClassNames(MutableType mutableType) {
		return new NamedType[] {
			getOutputClass(mutableType)
		};
	};

	private TypeElement getComponentType(TypeElement typeElement) {
		ThemeSupport themeSupportAnnotation = typeElement.getAnnotation(ThemeSupport.class);
		return AnnotationClassPropertyHarvester.getTypeOfClassProperty(themeSupportAnnotation, new AnnotationClassProperty<ThemeSupport>() {

			@Override
			public Class<?> getClassProperty(ThemeSupport annotation) {
				return annotation.widgetClass();
			}
		});
	}

	private String toString(Collection<Modifier> modifiers) {
		String result = "";
		
		for (Modifier modifier: modifiers) {
			result = modifier.toString() + " ";
		}
		
		return result;
	}

	protected void processSuperClass(TypeElement rootElement, Map<TypeElement, List<ExecutableElement>> methodCache, javax.lang.model.element.Element superClassElement, PrintWriter pw) {
		if (superClassElement == null ||
			superClassElement.equals(processingEnv.getElementUtils().getTypeElement(Object.class.getCanonicalName()))) {
			return;
		}

		if (superClassElement.getKind() != ElementKind.CLASS) {
			return;
		}
		
		Elements elementUtils = processingEnv.getElementUtils();
		
		TypeElement superClass = (TypeElement)superClassElement;
		
		if (!superClassElement.equals(processingEnv.getElementUtils().getTypeElement(Widget.class.getCanonicalName()))) {

			List<ExecutableElement> methods = ElementFilter.methodsIn(superClass.getEnclosedElements());
			
			for (ExecutableElement method: methods) {
				if (method.getModifiers().contains(Modifier.PUBLIC) && !method.getModifiers().contains(Modifier.FINAL) &&
					!method.getModifiers().contains(Modifier.STATIC)) {
	
					if (method.getSimpleName().toString().equals("getElement") && method.getParameters().size() == 0) {
						continue;
					}
					
					boolean isOverriden = false;
					
					for (Entry<TypeElement, List<ExecutableElement>> cachedTypeMethods: methodCache.entrySet()) {
						for (ExecutableElement cachedMethod: cachedTypeMethods.getValue()) {
							if (elementUtils.overrides(cachedMethod, method, cachedTypeMethods.getKey())) {
								isOverriden = true;
								break;
							}
						}
					}
					
					if (!isOverriden) {
						ExecutableType methodType = (ExecutableType)processingEnv.getTypeUtils().asMemberOf((DeclaredType)rootElement.asType(), method);
						
						List<ExecutableElement> cachedMethods = methodCache.get(superClass);
						if (cachedMethods == null) {
							cachedMethods = new ArrayList<ExecutableElement>();
							methodCache.put(superClass, cachedMethods);
						}
						cachedMethods.add(method);
						
						pw.print(toString(method.getModifiers()) + methodType.getReturnType().toString() + " " + method.getSimpleName().toString() + "(");
						int i = 0;
						
						for (TypeMirror parameterType : methodType.getParameterTypes()) {
							if (i > 0) {
								pw.print(", ");
							}
						
							VariableElement parameterElement = method.getParameters().get(i);
							i++;
							
							pw.print(parameterType.toString() + " " + parameterElement.getSimpleName());
						}
						pw.print(")");
						
						if (methodType.getThrownTypes() != null && methodType.getThrownTypes().size() > 0) {
							pw.print(" throws ");
							i = 0;
							for (TypeMirror thrownType: methodType.getThrownTypes()) {
								if (i > 0) {
									pw.print(", ");
								}
								pw.print(thrownType.toString());
								i++;
							}
						}
						pw.println(" {");
						pw.println("componentOperation = true;");
						if (!methodType.getReturnType().toString().toLowerCase().equals("void")) {
							pw.print(methodType.getReturnType().toString() + " result = ");
						}
						
						i = 0;
						pw.print("super." + method.getSimpleName().toString() + "(");
						for (VariableElement parameter : method.getParameters()) {
							if (i > 0) {
								pw.print(", ");
							}
							i++;
							pw.print(parameter.getSimpleName());
						}
						pw.println(");");
		
						pw.println("componentOperation = false;");
						if (!methodType.getReturnType().toString().toLowerCase().equals("void")) {
							pw.println("return result;");
						}
						pw.println("}");
						pw.println("");
					}
				}
			}
		}
		
		TypeMirror parent = superClass.getSuperclass();
		if (parent != null && parent.getKind().equals(TypeKind.DECLARED)) {
			processSuperClass(rootElement, methodCache, ((DeclaredType)parent).asElement(), pw);
		}
	}
	
	@Override
	protected void processElement(TypeElement element, NamedType outputClass, RoundEnvironment roundEnv, PrintWriter pw) {

		ThemeSupport themeSupportAnnotation = element.getAnnotation(ThemeSupport.class);

		MutableType componentType = ThemeComponentProcessor.getOutputClass(getNameTypes().toType(element));
		
		String componentName = "component";

		TypeElement componentClass = getComponentType(element);
		
		pw.println("private boolean componentOperation = false;");
		
		pw.println("private " + componentType.getSimpleName() + " " + componentName + ";");
		pw.println();
		pw.println("public " + outputClass.getSimpleName() + "() {");
		pw.println("this(new " + componentType.getSimpleName() + "());");
		pw.println("}");
		pw.println();
		pw.println("private " + outputClass.getSimpleName() + "(" + componentType.getSimpleName() + " component) {");
		pw.print("super((" + Element.class.getCanonicalName() + ")component." + themeSupportAnnotation.elementName());
		for (ComponentSpecificProcessor specificProcessor: specificProcessors) {
			if (specificProcessor.supports(componentClass)) {
				specificProcessor.process(Statement.SUPER_CONSTRUCTOR_ARGS, themeSupportAnnotation, pw);
			}
		}
		pw.println(");");
		pw.println("this.component = component;");
		
		for (ComponentSpecificProcessor specificProcessor: specificProcessors) {
			if (specificProcessor.supports(componentClass)) {
				specificProcessor.process(Statement.CONSTRUCTOR, themeSupportAnnotation, pw);
			}
		}
		
		pw.println("}");
		pw.println();
		pw.println("@Override");
		pw.println("public " + com.google.gwt.user.client.Element.class.getCanonicalName() + " getElement() {");
		pw.println("if (component == null) {");
		pw.println("return super.getElement();");
		pw.println("}");
		pw.println("if (componentOperation) {");
		pw.println("return (" + Element.class.getCanonicalName() + ")component." + themeSupportAnnotation.elementName() + ";");
		pw.println("}");
		pw.println("return component.getElement();");
		pw.println("}");

		for (ComponentSpecificProcessor specificProcessor: specificProcessors) {
			if (specificProcessor.supports(componentClass)) {
				specificProcessor.process(Statement.CLASS, themeSupportAnnotation, pw);
			}
		}

		TypeMirror superclass = componentClass.getSuperclass();
		
		if (superclass.getKind().equals(TypeKind.DECLARED)) {
			Map<TypeElement, List<ExecutableElement>> methodCache = new HashMap<TypeElement, List<ExecutableElement>>();
			processSuperClass(componentClass, methodCache, ((DeclaredType)superclass).asElement(), pw);
		}
	}
}