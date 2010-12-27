/**
 * 
 */
package sk.seges.acris.binding.jsr269;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Generated;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import sk.seges.acris.binding.client.processor.IBeanPropertyConverter;
import sk.seges.acris.binding.client.processor.PojoPropertyConverter;
import sk.seges.acris.binding.client.wrappers.BeanWrapper;
import sk.seges.acris.core.jsr269.AbstractConfigurableProcessor;
import sk.seges.acris.core.jsr269.ProcessorUtils;

/**
 * Generates bean wrapper interfaces for all relevant classes. The definition of which classes to process is following
 * the rule:
 * <ul>
 * <li>by default {@link sk.seges.acris.binding.client.annotations.BeanWrapper} annotated classes are taken</li>
 * <li>addition configuration is read from project's META-INF/bean-wrapper.properties file</li>
 * </ul>
 * Bean processor also generates constants for accessing each attribute in the bean in to order to write type-safe code.
 * Let's imagine that you have a bean User POJO with the following fields:
 * <ul>
 * <li>First name - represented by the property firstName</li>
 * <li>Last name - represented by the property lastName</li>
 * <li>Login - represented by the property login</li>
 * </ul>
 * and the BeanWrapper will contains following fields:
 * <ul>
 * <li>FIRST_NAME targeting "firstName" property</li>
 * <li>LAST_NAME targeting "lastName" property</li>
 * <li>LOGIN targeting "login" property</li>
 * </ul>
 * From now you can use constants from bean wrapper in order to reach type safe coding, e.g. UserBeanWrapper.FIRST_NAME
 * will reference User.getFirstName() method and when the property firstName will be renamed then also UserBeanWrapper
 * will be regenerated and you will get a compile error (when using the strings, determining the references is much more
 * harder)
 * 
 * @author eldzi
 * @author Peter Simun (simun@seges.sk)
 */
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedOptions({BeanWrapperProcessor.CONFIG_FILE_LOCATION})
public class BeanWrapperProcessor extends AbstractConfigurableProcessor {

	private static final String DEFAULT_CONFIG_FILE_LOCATION = "/META-INF/bean-wrapper.properties";

	public static final String BEAN_WRAPPER_SUFFIX = "BeanWrapper";

	public static final String BEAN_CLASS_NAME = "class_";

	private enum AccessType {
		METHOD, PROPERTY;
	}

	@Override
	public synchronized void init(ProcessingEnvironment pe) {
		super.init(pe);
		annotations.add(sk.seges.acris.binding.client.annotations.BeanWrapper.class.getCanonicalName());
	}

	@Override
	protected String getConfigurationFileLocation() {
		return DEFAULT_CONFIG_FILE_LOCATION;
	}

	protected String getBeanWrapperName(String beanName) {
		return beanName + BEAN_WRAPPER_SUFFIX;
	}

	@Override
	protected boolean processElement(Element element, RoundEnvironment roundEnv) {
		
		PrintWriter pw = null;
		
		try {
			TypeElement typeElement = (TypeElement) element;

			Name beanElementName = ((TypeElement) element).getQualifiedName();

			String beanPackageName = beanElementName.toString().substring(0, beanElementName.toString().lastIndexOf("."));

			Element enclosingElement = element.getEnclosingElement();
			while (enclosingElement != null) {
				if (!enclosingElement.getKind().equals(ElementKind.CLASS) && !enclosingElement.getKind().equals(ElementKind.INTERFACE)) {
					enclosingElement = null;
				} else {
					String enclosingName = ((TypeElement) enclosingElement).getQualifiedName().toString();
					beanPackageName = enclosingName.substring(0, enclosingName.lastIndexOf("."));
					enclosingElement = enclosingElement.getEnclosingElement();
				}
			}

			String simpleName = getBeanWrapperName(beanElementName.toString().replace(beanPackageName, "").replace(".", ""));

			JavaFileObject createSourceFile = processingEnv.getFiler().createSourceFile(beanPackageName + "." + simpleName, element);
			OutputStream os = createSourceFile.openOutputStream();
			pw = new PrintWriter(os);

			pw.println("package " + beanPackageName + ";");
			pw.println();

			for (String importName : getImports()) {
				pw.println("import " + importName + ";");
			}

			pw.println("@" + Generated.class.getCanonicalName() + "(\"" + BeanWrapperProcessor.class.getCanonicalName() + "\")");

			//			if (element.getKind().equals(ElementKind.INTERFACE)) {
			//				pw.println("public interface " + simpleName + " {");
			//			} else {
			pw.println("public interface " + simpleName + " extends " + BeanWrapper.class.getCanonicalName() + "<" + beanElementName.toString() + "> {");
			//			}
			pw.println();

			TypeElement el = typeElement;

			HashSet<String> hierarchyTypes = new HashSet<String>();

			Set<String> classConstantsCache = new HashSet<String>();

			processClass(classConstantsCache, hierarchyTypes, pw, el);

			while (el.getSuperclass() != null) {
				if (el.getSuperclass() instanceof DeclaredType) {
					el = (TypeElement) ((DeclaredType) el.getSuperclass()).asElement();

					processClass(classConstantsCache, hierarchyTypes, pw, el);
				} else {
					break;
				}
			}

			pw.println("}");
			pw.flush();

		} catch (IOException e) {
			processingEnv.getMessager().printMessage(Kind.ERROR, "Unable to process element = ", element);
		} finally {
			if (pw != null) {
				pw.close();
			}
		}

		return true;
	}

	protected String[] appendImport(String[] imports, String importName) {
		List<String> result = new ArrayList<String>();

		for (String inputImport : imports) {
			if (inputImport.equals(importName)) {
				return imports;
			}
			result.add(inputImport);
		}

		result.add(importName);

		return result.toArray(new String[] {});
	}

	protected String[] getImports() {
		return new String[] {/*
							 * BeanWrapper.class.getName(), SimpleClassMetaDescriptor.class.getName(),
							 * PropertyMetaDescriptor.class.getName()
							 */};
	}

	private static final String GETTER_PREFIX = "get";
	private static final String IS_PREFIX = "is";

	private void writeMethodsFromClass(Set<String> classConstantsCache, Set<String> hierarchyTypes, PrintWriter pw, Element element, Set<IBeanPropertyConverter> converterInstances, String prefix,
			int level) {
		List<? extends Element> methodsOfClass = ElementFilter.methodsIn(element.getEnclosedElements());

		for (Element methodElement : methodsOfClass) {
			if (methodElement.getModifiers().contains(Modifier.STATIC) || methodElement.getModifiers().contains(Modifier.PRIVATE)
					|| methodElement.getModifiers().contains(Modifier.PROTECTED)) {
				continue;
			}

			if (!(methodElement instanceof ExecutableElement)) {
				continue;
			}

			ExecutableElement method = (ExecutableElement) methodElement;

			TypeMirror typeMirror = methodElement.asType();
			String simpleMethodName = methodElement.getSimpleName().toString();
			if (simpleMethodName.length() == 0 || !(simpleMethodName.startsWith(GETTER_PREFIX) || simpleMethodName.startsWith(IS_PREFIX))) {
				//only getters are interesting
				continue;
			}

			int count = 3;

			String setterMethodName = "set";
			if (simpleMethodName.startsWith(GETTER_PREFIX)) {
				setterMethodName = setterMethodName + simpleMethodName.substring(GETTER_PREFIX.length());
			} else if (simpleMethodName.startsWith(IS_PREFIX)) {
				count = 2;
				setterMethodName = setterMethodName + simpleMethodName.substring(IS_PREFIX.length());
			}
			ExecutableElement setterMethod = ProcessorUtils.getMethodByParameterType(setterMethodName, element, 0, method.getReturnType());
			if (setterMethod == null) {
				//setter method is not accessible
				continue;
			}

			writeForProperty(classConstantsCache, hierarchyTypes, pw, typeMirror, element, AccessType.METHOD, getPropertyName(simpleMethodName, count), converterInstances, prefix, level);
		}
	}

	private String getPropertyName(String simpleMethodName, int count) {
		if (simpleMethodName.length() > count) {
			return ("" + simpleMethodName.charAt(count)).toLowerCase() + simpleMethodName.substring(count + 1);
		}
		return ("" + simpleMethodName.charAt(count)).toLowerCase();
	}

	private void writeFieldsFromClass(Set<String> classConstantsCache, Set<String> hierarchyTypes, PrintWriter pw, Element element, Set<IBeanPropertyConverter> converterInstances, String prefix,
			int level) {
		List<? extends Element> fieldsOfClass = ElementFilter.fieldsIn(element.getEnclosedElements());

		//We are not going to 
		for (Element fieldElement : fieldsOfClass) {
			if (fieldElement.getModifiers().contains(Modifier.STATIC) || fieldElement.getModifiers().contains(Modifier.PRIVATE)
					|| fieldElement.getModifiers().contains(Modifier.PROTECTED) || fieldElement.getModifiers().contains(Modifier.FINAL)) {
				continue;
			}

			TypeMirror typeMirror = fieldElement.asType();
			String fieldName = fieldElement.getSimpleName().toString();

			String getter = GETTER_PREFIX + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1) + "()";
			if (ProcessorUtils.hasMethod(getter, element)) {
				//this will be processed by another routine.
				processingEnv.getMessager().printMessage(Kind.WARNING,
						"Field " + fieldName + " is accessible by public modifier and also using the " + getter + " method.", element);
				continue;
			}

			writeForProperty(classConstantsCache, hierarchyTypes, pw, typeMirror, element, AccessType.PROPERTY, fieldElement.getSimpleName().toString(), converterInstances, prefix, level);
		}
	}

	private Set<IBeanPropertyConverter> getSamePolicyNotProcessedConverters(String property, String convertedProperty,
			Set<IBeanPropertyConverter> processedConverters, Set<IBeanPropertyConverter> availableConverters) {
		Set<IBeanPropertyConverter> result = new HashSet<IBeanPropertyConverter>();

		for (IBeanPropertyConverter beanPropertyConverter : availableConverters) {
			if (!(processedConverters.contains(beanPropertyConverter))) {
				if (convertedProperty.equals(beanPropertyConverter.getConvertedPropertyName(property))) {
					result.add(beanPropertyConverter);
				}
			}
		}

		return result;
	}

	private boolean writeHierarchy(Set<String> classConstantsCache, Set<String> hierarchyTypes, PrintWriter pw, TypeElement classTypeElement, String property,
			Set<IBeanPropertyConverter> converterInstances, String prefix, int level) {

		Set<IBeanPropertyConverter> processedConverters = new HashSet<IBeanPropertyConverter>();

		boolean result = false;

		hierarchyTypes.add(property);

		for (IBeanPropertyConverter converterInstance : converterInstances) {
			if (processedConverters.contains(converterInstance)) {
				continue;
			}

			String convertedName = converterInstance.getConvertedPropertyName(property);

			Set<IBeanPropertyConverter> selectedConverters = getSamePolicyNotProcessedConverters(property, convertedName, processedConverters,
					converterInstances);

			Set<String> cache = new HashSet<String>();

			for (IBeanPropertyConverter selectedConverter : selectedConverters) {
				processedConverters.add(selectedConverter);

				if (!selectedConverter.supportsHierarchy()) {
					continue;
				}
				
				classConstantsCache.add(convertedName);
				
				pw.println(indent("public static interface " + convertedName + " {", level));
				pw.println();

				//				String convertedThis = selectedConverter.getConvertedPropertyName("this");
				String convertedThis = "THIS";

				if (cache.add(convertedThis)) {
					pw.println(indent(
							"public static " + String.class.getSimpleName() + " " + convertedThis + " = \"" + prefix
									+ selectedConverter.getConvertedPropertyValue(property) + "\";", level + 1));
					pw.println();
				}

				processClass(new HashSet<String>(), hierarchyTypes, pw, classTypeElement, selectedConverters, prefix + property + ".", level + 1);

				pw.println(indent("}", level));
				pw.println();

				result = true;
			}
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	private boolean writeForProperty(Set<String> classConstantsCache, Set<String> hierarchyTypes, PrintWriter pw, DeclaredType declaredType, Element element, String property,
			Set<IBeanPropertyConverter> converterInstances, String prefix, int level) {
		final Element classElement = declaredType.asElement();
		TypeElement classTypeElement = (TypeElement) classElement;
		for (String annotationName : annotations) {
			try {
				Annotation annotation = classTypeElement.getAnnotation((Class<Annotation>) Class.forName(annotationName));
				if (annotation != null) {
					//this is supported bean wrapper class
					return writeHierarchy(classConstantsCache, hierarchyTypes, pw, classTypeElement, property, converterInstances, prefix, level);
				}
			} catch (ClassNotFoundException e) {
				processingEnv.getMessager().printMessage(Kind.WARNING, "Unable to find annotation " + annotationName + " on the class path", element);
			}
		}

		return false;
	}

	private void writeForProperty(Set<String> classConstantsCache, Set<String> hierarchyTypes, PrintWriter pw, TypeMirror typeMirror, Element element, AccessType accessType, String property,
			Set<IBeanPropertyConverter> converterInstances, String prefix, int level) {
		boolean interfaceGenerated = false;

		if (!hierarchyTypes.contains(property)) {
			//cycle not detected
			if (!typeMirror.getKind().isPrimitive()) {
				if (typeMirror.getKind() == TypeKind.DECLARED && accessType.equals(AccessType.PROPERTY)) {
					interfaceGenerated = writeForProperty(classConstantsCache, hierarchyTypes, pw, (DeclaredType) typeMirror, element, property, converterInstances, prefix, level);
				} else if (typeMirror.getKind() == TypeKind.EXECUTABLE && accessType.equals(AccessType.METHOD)) {
					final TypeMirror returnTypeElement = ((ExecutableType) typeMirror).getReturnType();
					if (returnTypeElement != null && returnTypeElement.getKind() == TypeKind.DECLARED) {
						interfaceGenerated = writeForProperty(classConstantsCache, hierarchyTypes, pw, (DeclaredType) returnTypeElement, element, property, converterInstances, prefix, level);
					}
				}
			}
		}

		if (!interfaceGenerated) {
			writeConstant(classConstantsCache, pw, accessType, property, converterInstances, prefix, level);
		}
	}

	private void writeConstant(Set<String> classConstantsCache, PrintWriter pw, AccessType accessType, String property, Set<IBeanPropertyConverter> converterInstances, String prefix, int level) {

		for (IBeanPropertyConverter beanPropertyConverter : converterInstances) {
			if ((accessType.equals(AccessType.PROPERTY) && beanPropertyConverter.handleFields())
					|| (accessType.equals(AccessType.METHOD) && beanPropertyConverter.handleMethods())) {

				String convertedName = beanPropertyConverter.getConvertedPropertyName(property);
				
				if (!classConstantsCache.contains(convertedName)) {
					
					classConstantsCache.add(convertedName);

					pw.println(indent("public static " + String.class.getSimpleName() + " " + convertedName + " = \""
							+ beanPropertyConverter.getConvertedPropertyValue(prefix + property) + "\";", level));
					pw.println();
				}
			}
		}
	}

	private void processClass(Set<String> classConstantsCache, Set<String> hierarchyTypes, PrintWriter pw, Element element) {
		processClass(classConstantsCache, hierarchyTypes, pw, element, null, "", 1);
	}

	private Set<IBeanPropertyConverter> createInstances(List<Class<? extends IBeanPropertyConverter>> converters, Element element) {
		Set<IBeanPropertyConverter> converterInstances = new HashSet<IBeanPropertyConverter>();

		for (Class<? extends IBeanPropertyConverter> converter : converters) {
			try {
				converterInstances.add(converter.getConstructor().newInstance());
			} catch (Exception e) {
				processingEnv.getMessager().printMessage(Kind.WARNING,
						"Unable to instantiate " + converter.getName() + " using default constructor. Naming converter will be skipped", element);
			}
		}

		return converterInstances;
	}

	private void processClass(Set<String> classConstantsCache, Set<String> hierarchyTypes, PrintWriter pw, Element element, Set<IBeanPropertyConverter> selectedConverters, String prefix, int level) {

		Set<IBeanPropertyConverter> converterInstances = selectedConverters;

		if (converterInstances == null) {
			sk.seges.acris.binding.client.annotations.BeanWrapper annotation = ((TypeElement) element)
					.getAnnotation(sk.seges.acris.binding.client.annotations.BeanWrapper.class);
			if (annotation != null) {
				AnnotationMirror beanWrapperAnnotation = ProcessorUtils
						.containsAnnotation(element, sk.seges.acris.binding.client.annotations.BeanWrapper.class);
				if (beanWrapperAnnotation != null) {
					List<Class<? extends IBeanPropertyConverter>> beanPropertyConverter = ProcessorUtils.convertToList(ProcessorUtils.getAnnotationValue(
							beanWrapperAnnotation, "beanPropertyConverter"));
					converterInstances = createInstances(beanPropertyConverter, element);
				} else {
					converterInstances = new HashSet<IBeanPropertyConverter>();
				}
			} else {
				converterInstances = new HashSet<IBeanPropertyConverter>();
			}
		} else {
			converterInstances = new HashSet<IBeanPropertyConverter>();
		}

		if (converterInstances.size() == 0) {
			converterInstances.add(new PojoPropertyConverter());
		}
		
		writeFieldsFromClass(classConstantsCache, hierarchyTypes, pw, element, converterInstances, prefix, level);
		writeMethodsFromClass(classConstantsCache, hierarchyTypes, pw, element, converterInstances, prefix, level);
	}

	private String indent(String text, int level) {
		String result = "";

		for (int i = 0; i < level; i++) {
			result += "	";
		}
		return result + text;
	}
}