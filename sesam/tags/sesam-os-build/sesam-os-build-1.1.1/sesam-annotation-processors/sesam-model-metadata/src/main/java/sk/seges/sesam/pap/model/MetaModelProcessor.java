/**
 * 
 */
package sk.seges.sesam.pap.model;

import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.model.api.MutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.structure.api.PackageValidatorProvider;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.model.metadata.annotation.MetaModel;
import sk.seges.sesam.model.metadata.strategy.PojoPropertyConverter;
import sk.seges.sesam.model.metadata.strategy.api.ModelPropertyConverter;

/**
 * Generates meta model interfaces for all relevant classes. The definition of which classes to process is following
 * the rule:
 * <ul>
 * <li>by default {@link MetaModel} annotated classes are taken</li>
 * <li>addition configuration is read from project's META-INF/meta-model.properties file</li>
 * </ul>
 * Bean processor also generates constants for accessing each attribute in the bean in to order to write type-safe code.
 * Let's imagine that you have a bean User POJO with the following fields:
 * <ul>
 * <li>First name - represented by the property firstName</li>
 * <li>Last name - represented by the property lastName</li>
 * <li>Login - represented by the property login</li>
 * </ul>
 * and the MetaModel will contains following fields:
 * <ul>
 * <li>FIRST_NAME targeting "firstName" property</li>
 * <li>LAST_NAME targeting "lastName" property</li>
 * <li>LOGIN targeting "login" property</li>
 * </ul>
 * From now you can use constants from meta model in order to reach type safe coding, e.g. UserMetaModel.FIRST_NAME
 * will reference User.getFirstName() method and when the property firstName will be renamed then also UserMetaModel
 * will be regenerated and you will get a compile error (when using the strings, determining the references is much more
 * harder)
 * 
 * @author eldzi
 * @author Peter Simun (simun@seges.sk)
 */
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedOptions({MetaModelProcessor.CONFIG_FILE_LOCATION})
public class MetaModelProcessor extends AbstractConfigurableProcessor {

	private static final String DEFAULT_CONFIG_FILE_LOCATION = "/META-INF/meta-model.properties";

	public static final String META_MODEL_SUFFIX = "MetaModel";

	public static final String BEAN_CLASS_NAME = "class_";

	private enum AccessType {
		METHOD, PROPERTY;
	}

	@Override
	protected Type[] getConfigurationTypes(DefaultConfigurationType type, TypeElement typeElement) {
		switch (type) {
			case PROCESSING_ANNOTATIONS:
				return new Type[] { MetaModel.class };
		}
		return super.getConfigurationTypes(type, typeElement);
	}
	
	@Override
	protected String getConfigurationFileLocation() {
		return DEFAULT_CONFIG_FILE_LOCATION;
	}

	protected PackageValidatorProvider getPackageValidatorProvider() {
		return new DefaultPackageValidatorProvider();
	}

	public static NamedType getOutputClass(MutableType inputClass, PackageValidatorProvider packageValidatorProvider) {
		return inputClass.addClassSufix(META_MODEL_SUFFIX);
	}
	
	@Override
	protected NamedType[] getTargetClassNames(MutableType inputClass) {
		return new NamedType[] { 
				getOutputClass(inputClass, getPackageValidatorProvider()) 
		};
	}

	@Override
	protected void processElement(TypeElement element, NamedType outputClass, RoundEnvironment roundEnv, PrintWriter pw) {
		HashSet<String> hierarchyTypes = new HashSet<String>();
		Set<String> classConstantsCache = new HashSet<String>();

		processClass(classConstantsCache, hierarchyTypes, pw, element);

		while (element.getSuperclass() != null) {
			if (element.getSuperclass() instanceof DeclaredType) {
				element = (TypeElement) ((DeclaredType) element.getSuperclass()).asElement();

				processClass(classConstantsCache, hierarchyTypes, pw, element);
			} else {
				break;
			}
		}
	}
	
	private static final String GETTER_PREFIX = "get";
	private static final String IS_PREFIX = "is";

	private void writeMethodsFromClass(Set<String> classConstantsCache, Set<String> hierarchyTypes, PrintWriter pw, Element element, Set<ModelPropertyConverter> converterInstances, String prefix,
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
			ExecutableElement setterMethod = ProcessorUtils.getMethodByParameterType(setterMethodName, element, 0, method.getReturnType(), processingEnv.getTypeUtils());
			if (setterMethod == null) {
				//setter method is not accessible
				continue;
			}

			writeForProperty(classConstantsCache, hierarchyTypes, pw, typeMirror, element, AccessType.METHOD, getPropertyName(simpleMethodName, count), converterInstances, prefix, level);
		}
	}

	protected ElementKind getElementKind() {
		return ElementKind.INTERFACE;
	};
	
	private String getPropertyName(String simpleMethodName, int count) {
		if (simpleMethodName.length() > count) {
			return ("" + simpleMethodName.charAt(count)).toLowerCase() + simpleMethodName.substring(count + 1);
		}
		return ("" + simpleMethodName.charAt(count)).toLowerCase();
	}

	private void writeFieldsFromClass(Set<String> classConstantsCache, Set<String> hierarchyTypes, PrintWriter pw, Element element, Set<ModelPropertyConverter> converterInstances, String prefix,
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

	private Set<ModelPropertyConverter> getSamePolicyNotProcessedConverters(String property, String convertedProperty,
			Set<ModelPropertyConverter> processedConverters, Set<ModelPropertyConverter> availableConverters) {
		Set<ModelPropertyConverter> result = new HashSet<ModelPropertyConverter>();

		for (ModelPropertyConverter beanPropertyConverter : availableConverters) {
			if (!(processedConverters.contains(beanPropertyConverter))) {
				if (convertedProperty.equals(beanPropertyConverter.getConvertedPropertyName(property))) {
					result.add(beanPropertyConverter);
				}
			}
		}

		return result;
	}

	private boolean writeHierarchy(Set<String> classConstantsCache, Set<String> hierarchyTypes, PrintWriter pw, TypeElement classTypeElement, String property,
			Set<ModelPropertyConverter> converterInstances, String prefix, int level) {

		Set<ModelPropertyConverter> processedConverters = new HashSet<ModelPropertyConverter>();

		boolean result = false;

		hierarchyTypes.add(property);

		for (ModelPropertyConverter converterInstance : converterInstances) {
			if (processedConverters.contains(converterInstance)) {
				continue;
			}

			String convertedName = converterInstance.getConvertedPropertyName(property);

			Set<ModelPropertyConverter> selectedConverters = getSamePolicyNotProcessedConverters(property, convertedName, processedConverters,
					converterInstances);

			Set<String> cache = new HashSet<String>();

			for (ModelPropertyConverter selectedConverter : selectedConverters) {
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
							"public static final " + String.class.getSimpleName() + " " + convertedThis + " = \"" + prefix
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
			Set<ModelPropertyConverter> converterInstances, String prefix, int level) {
		final Element classElement = declaredType.asElement();
		TypeElement classTypeElement = (TypeElement) classElement;
		for (sk.seges.sesam.core.pap.model.api.NamedType type : getMergedConfiguration(DefaultConfigurationType.PROCESSING_ANNOTATIONS, classTypeElement)) {

			Class<Annotation> annotationClass;
			try {
				annotationClass = (Class<Annotation>) Class.forName(type.getQualifiedName());
			
				Annotation annotation = classTypeElement.getAnnotation(annotationClass);
				if (annotation != null) {
					//this is supported meta model class
					return writeHierarchy(classConstantsCache, hierarchyTypes, pw, classTypeElement, property, converterInstances, prefix, level);
				}
			} catch (ClassNotFoundException e) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "Unable to find annotation class " + type.getQualifiedName());
				e.printStackTrace();
			}
		}
		
		return false;
	}

	private void writeForProperty(Set<String> classConstantsCache, Set<String> hierarchyTypes, PrintWriter pw, TypeMirror typeMirror, Element element, AccessType accessType, String property,
			Set<ModelPropertyConverter> converterInstances, String prefix, int level) {
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

	private void writeConstant(Set<String> classConstantsCache, PrintWriter pw, AccessType accessType, String property, Set<ModelPropertyConverter> converterInstances, String prefix, int level) {

		for (ModelPropertyConverter beanPropertyConverter : converterInstances) {
			if ((accessType.equals(AccessType.PROPERTY) && beanPropertyConverter.handleFields())
					|| (accessType.equals(AccessType.METHOD) && beanPropertyConverter.handleMethods())) {

				String convertedName = beanPropertyConverter.getConvertedPropertyName(property);
				
				if (!classConstantsCache.contains(convertedName)) {
					
					classConstantsCache.add(convertedName);

					pw.println(indent("public final static " + String.class.getSimpleName() + " " + convertedName + " = \""
							+ beanPropertyConverter.getConvertedPropertyValue(prefix + property) + "\";", level));
					pw.println();
				}
			}
		}
	}

	private void processClass(Set<String> classConstantsCache, Set<String> hierarchyTypes, PrintWriter pw, Element element) {
		processClass(classConstantsCache, hierarchyTypes, pw, element, null, "", 1);
	}

	private Set<ModelPropertyConverter> createInstances(List<Class<? extends ModelPropertyConverter>> converters, Element element) {
		Set<ModelPropertyConverter> converterInstances = new HashSet<ModelPropertyConverter>();

		for (Class<? extends ModelPropertyConverter> converter : converters) {
			try {
				converterInstances.add(converter.getConstructor().newInstance());
			} catch (Exception e) {
				processingEnv.getMessager().printMessage(Kind.WARNING,
						"Unable to instantiate " + converter.getName() + " using default constructor. Naming converter will be skipped", element);
			}
		}

		return converterInstances;
	}

	private void processClass(Set<String> classConstantsCache, Set<String> hierarchyTypes, PrintWriter pw, Element element, Set<ModelPropertyConverter> selectedConverters, String prefix, int level) {

		Set<ModelPropertyConverter> converterInstances = selectedConverters;

		if (converterInstances == null) {
			MetaModel annotation = ((TypeElement) element).getAnnotation(MetaModel.class);
			if (annotation != null) {
				AnnotationMirror metaModelAnnotation = ProcessorUtils.containsAnnotation(element, MetaModel.class);
				if (metaModelAnnotation != null) {
					List<Class<? extends ModelPropertyConverter>> beanPropertyConverter = ProcessorUtils.convertToList(ProcessorUtils.getAnnotationValue(
							metaModelAnnotation, "beanPropertyConverter"));
					converterInstances = createInstances(beanPropertyConverter, element);
				} else {
					converterInstances = new HashSet<ModelPropertyConverter>();
				}
			} else {
				converterInstances = new HashSet<ModelPropertyConverter>();
			}
		} else {
			converterInstances = new HashSet<ModelPropertyConverter>();
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