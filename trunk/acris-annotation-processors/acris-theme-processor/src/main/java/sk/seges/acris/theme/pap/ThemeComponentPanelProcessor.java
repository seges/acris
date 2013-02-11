package sk.seges.acris.theme.pap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.processing.ProcessingEnvironment;
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
import sk.seges.acris.theme.pap.configurer.ThemeProcessorConfigurer;
import sk.seges.acris.theme.pap.model.ThemeComponentType;
import sk.seges.acris.theme.pap.model.ThemeConfigurationType;
import sk.seges.acris.theme.pap.specific.AbstractComponentSpecificProcessor.Statement;
import sk.seges.acris.theme.pap.specific.ComponentSpecificProcessor;
import sk.seges.acris.theme.pap.specific.ThemeCheckBoxProcessor;
import sk.seges.acris.theme.pap.specific.ThemeContext;
import sk.seges.acris.theme.pap.specific.ThemeDefaultProcessor;
import sk.seges.acris.theme.pap.specific.ThemeDialogBoxProcessor;
import sk.seges.acris.theme.pap.specific.ThemeImageCheckBoxProcessor;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.processor.MutableAnnotationProcessor;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

import com.google.gwt.user.client.ui.SimplePanel;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ThemeComponentPanelProcessor extends MutableAnnotationProcessor {

	private List<ComponentSpecificProcessor> specificProcessors = new LinkedList<ComponentSpecificProcessor>();
	
	public ThemeComponentPanelProcessor() {
		specificProcessors.add(new ThemeDefaultProcessor());
		specificProcessors.add(new ThemeImageCheckBoxProcessor());
		specificProcessors.add(new ThemeCheckBoxProcessor());
		specificProcessors.add(new ThemeDialogBoxProcessor());
	}

	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new ThemeProcessorConfigurer();
	}	

	@Override
	public synchronized void init(ProcessingEnvironment pe) {
		super.init(pe);
		for (ComponentSpecificProcessor specificProcessor: specificProcessors) {
			specificProcessor.init(pe);
		}
	}

	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] {
				new ThemeConfigurationType(context.getTypeElement(), processingEnv).getThemePanel()
		};
	}

	private String toString(Collection<Modifier> modifiers) {
		String result = "";
		
		for (Modifier modifier: modifiers) {
			result += modifier.toString() + " ";
		}
		
		return result;
	}

	protected void processSuperClass(TypeElement rootElement, Map<TypeElement, List<ExecutableElement>> methodCache, javax.lang.model.element.Element superClassElement, FormattedPrintWriter pw) {
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
							
							MutableTypeMirror mutableType = processingEnv.getTypeUtils().toMutableType(parameterType);
							pw.print(processingEnv.getTypeUtils().stripTypeVariableTypes(mutableType), " " + parameterElement.getSimpleName());
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
	protected void processElement(ProcessorContext context) {

		TypeElement element = context.getTypeElement();
		FormattedPrintWriter pw = context.getPrintWriter();
		
		ThemeSupport themeSupportAnnotation = element.getAnnotation(ThemeSupport.class);

		ThemeConfigurationType configurationType = new ThemeConfigurationType(element, processingEnv);
		ThemeComponentType componentType = configurationType.getThemeComponent();
		
		String componentName = "component";

		TypeElement componentClass = configurationType.getWidgetType();
				
		pw.println("private boolean componentOperation = false;");
		
		Theme themeAnnotation = element.getAnnotation(Theme.class);
		
		ThemeContext themeContext = new ThemeContext();
		if (themeAnnotation != null) {
			themeContext.setThemeName(themeAnnotation.value());
		} else {
			themeContext.setThemeName("UNDEFINED");
		}
		themeContext.setThemeSupport(themeSupportAnnotation);

		pw.println("private ", componentType, " " + componentName + ";");
		pw.println();
		pw.println("public " + context.getOutputType().getSimpleName() + "() {");
		pw.println("this(new " + componentType.getSimpleName() + "());");
		pw.println("}");
		pw.println();
		pw.println("private " + context.getOutputType().getSimpleName() + "(" + componentType.getSimpleName() + " component) {");

		TypeElement superElement = null;
		
		if (processingEnv.getTypeUtils().isSubtype(element.asType(), componentClass.asType())) {
			superElement = element;
		} else {
			superElement = componentClass;
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
			pw.println(SimplePanel.class, " panel = new ", SimplePanel.class, "((",
					com.google.gwt.user.client.Element.class, ")component." + themeSupportAnnotation.elementName() + ") {");
			pw.println("public ", SimplePanel.class, " initialize() {");
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