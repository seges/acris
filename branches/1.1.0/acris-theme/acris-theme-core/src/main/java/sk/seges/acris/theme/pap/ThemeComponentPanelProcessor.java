package sk.seges.acris.theme.pap;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
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

import sk.seges.acris.theme.client.annotation.Theme;
import sk.seges.acris.theme.client.annotation.ThemeSupport;
import sk.seges.acris.theme.pap.specific.AbstractComponentSpecificProcessor.Statement;
import sk.seges.acris.theme.pap.specific.ComponentSpecificProcessor;
import sk.seges.acris.theme.pap.specific.ThemeCheckBoxProcessor;
import sk.seges.acris.theme.pap.specific.ThemeContext;
import sk.seges.acris.theme.pap.specific.ThemeDefaultProcessor;
import sk.seges.acris.theme.pap.specific.ThemeDialogBoxProcessor;
import sk.seges.acris.theme.pap.specific.ThemeImageCheckBoxProcessor;
import sk.seges.acris.theme.pap.util.AnnotationClassPropertyHarvester;
import sk.seges.acris.theme.pap.util.AnnotationClassPropertyHarvester.AnnotationClassProperty;
import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.configuration.api.OutputDefinition;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.utils.ListUtils;

import com.google.gwt.user.client.ui.SimplePanel;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ThemeComponentPanelProcessor extends AbstractConfigurableProcessor {

	private List<ComponentSpecificProcessor> specificProcessors = new LinkedList<ComponentSpecificProcessor>();
	
	protected ElementKind getElementKind() {
		return ElementKind.CLASS;
	}
	
	public ThemeComponentPanelProcessor() {
		specificProcessors.add(new ThemeDefaultProcessor());
		specificProcessors.add(new ThemeImageCheckBoxProcessor());
		specificProcessors.add(new ThemeCheckBoxProcessor());
		specificProcessors.add(new ThemeDialogBoxProcessor());
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
			SimplePanel.class
		};
		
		List<Type> importTypes = new ArrayList<Type>();
		
		for (ComponentSpecificProcessor specificProcessor: specificProcessors) {
			TypeElement componentClass = getComponentType(typeElement);
			if (specificProcessor.supports(componentClass)) {
				ListUtils.add(importTypes, specificProcessor.getImports());
			}
		}
		ListUtils.add(importTypes, result);
		return importTypes.toArray(new Type[] {});
	}
	
	public static final ImmutableType getOutputClass(ImmutableType mutableType) {
		return mutableType.addClassSufix("Panel");
	}

	@Override
	protected Type[] getOutputDefinition(OutputDefinition type, TypeElement typeElement) {
		switch (type) {
			case OUTPUT_SUPERCLASS:
				
				TypeElement componentTypeElement = getComponentType(typeElement);
				if (processingEnv.getTypeUtils().isSubtype(typeElement.asType(), componentTypeElement.asType())) {
					return new Type[] { getNameTypes().toType(typeElement)};
				}
				return new Type[] { getNameTypes().toType(componentTypeElement)};
		}
		return super.getOutputDefinition(type, typeElement);
	}

	@Override
	protected NamedType[] getTargetClassNames(ImmutableType mutableType) {
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
			result += modifier.toString() + " ";
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
		
		if (!superClass.asType().getKind().equals(ElementKind.INTERFACE)) {

			List<ExecutableElement> methods = ElementFilter.methodsIn(superClass.getEnclosedElements());
			
			for (ExecutableElement method: methods) {
				
				ExecutableType methodType = (ExecutableType)processingEnv.getTypeUtils().asMemberOf((DeclaredType)rootElement.asType(), method);
				
				List<ExecutableElement> cachedMethods = methodCache.get(superClass);
				if (cachedMethods == null) {
					cachedMethods = new ArrayList<ExecutableElement>();
					methodCache.put(superClass, cachedMethods);
				}
				cachedMethods.add(method);

				if (method.getModifiers().contains(Modifier.PUBLIC) && !method.getModifiers().contains(Modifier.FINAL) &&
					!method.getModifiers().contains(Modifier.STATIC) && !method.getModifiers().contains(Modifier.ABSTRACT)) {

					boolean ignoredMethod = false;
					
					for (ComponentSpecificProcessor specificProcessor: specificProcessors) {
						if (specificProcessor.supports(rootElement) && specificProcessor.ignoreMethod(method)) {
							ignoredMethod = true;
							break;
						}
					}

					if (ignoredMethod) {
						continue;
					}
					
					boolean isComponentMethod = true;
					
					for (ComponentSpecificProcessor specificProcessor: specificProcessors) {
						if (specificProcessor.supports(rootElement) && !specificProcessor.isComponentMethod(method)) {
							isComponentMethod = false;
							break;
						}
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
						pw.println("boolean previousComponentOperation = componentOperation;");
						pw.println("componentOperation = " + isComponentMethod + ";");
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
		
						pw.println("componentOperation = previousComponentOperation;");
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

		ImmutableType componentType = ThemeComponentProcessor.getOutputClass(getNameTypes().toImmutableType(element));
		
		String componentName = "component";

		TypeElement componentClass = getComponentType(element);
				
		pw.println("private boolean componentOperation = false;");
		
		Theme themeAnnotation = element.getAnnotation(Theme.class);
		
		ThemeContext themeContext = new ThemeContext();
		if (themeAnnotation != null) {
			themeContext.setThemeName(themeAnnotation.value());
		} else {
			themeContext.setThemeName("UNDEFINED");
		}
		themeContext.setThemeSupport(themeSupportAnnotation);

		pw.println("private " + componentType.getSimpleName() + " " + componentName + ";");
		pw.println();
		pw.println("public " + outputClass.getSimpleName() + "() {");
		pw.println("this(new " + componentType.getSimpleName() + "());");
		pw.println("}");
		pw.println();
		pw.println("private " + outputClass.getSimpleName() + "(" + componentType.getSimpleName() + " component) {");

		TypeElement componentTypeElement = getComponentType(element);
		
		TypeElement superElement = null;
		
		if (processingEnv.getTypeUtils().isSubtype(element.asType(), componentTypeElement.asType())) {
			superElement = element;
		} else {
			superElement = componentTypeElement;
		}
		
		List<ExecutableElement> constructors = ElementFilter.constructorsIn(superElement.getEnclosedElements());
		
		boolean canAdaptElement = false;
		
		for (ExecutableElement constructor: constructors) {
			if (constructor.getParameters() != null && constructor.getParameters().size() > 0 &&
				(constructor.getParameters().get(0).asType().toString().equals(com.google.gwt.user.client.Element.class.getCanonicalName()) ||
				 constructor.getParameters().get(0).asType().toString().equals(com.google.gwt.dom.client.Element.class.getCanonicalName()))) {
				canAdaptElement = true;
				break;
			}
		}
		
		pw.print("super(");

		if (canAdaptElement) {
			for (ComponentSpecificProcessor specificProcessor: specificProcessors) {
				if (specificProcessor.supports(componentClass)) {
					specificProcessor.process(Statement.SUPER_CONSTRUCTOR_ARGS, themeContext, pw);
				}
			}
		}
		pw.println(");");

		if (!canAdaptElement) {
			pw.println(SimplePanel.class.getSimpleName() + " panel = new " + SimplePanel.class.getSimpleName() + "((" +
					com.google.gwt.user.client.Element.class.getCanonicalName() + ")component." + themeSupportAnnotation.elementName() + ") {");
			pw.println("public " + SimplePanel.class.getSimpleName() + " initialize() {");
			pw.println("onAttach();");
			pw.println("return this;");
			pw.println("}");
			pw.println("}.initialize();");
			pw.println("panel.add(this);");
		}
		
		for (ComponentSpecificProcessor specificProcessor: specificProcessors) {
			if (specificProcessor.supports(componentClass)) {
				specificProcessor.process(Statement.CONSTRUCTOR, themeContext, pw);
			}
		}
		
		pw.println("}");
		pw.println();

		for (ComponentSpecificProcessor specificProcessor: specificProcessors) {
			if (specificProcessor.supports(componentClass)) {
				specificProcessor.process(Statement.CLASS, themeContext, pw);
			}
		}

		Map<TypeElement, List<ExecutableElement>> methodCache = new HashMap<TypeElement, List<ExecutableElement>>();
		processSuperClass(componentClass, methodCache, componentClass, pw);
	}
}