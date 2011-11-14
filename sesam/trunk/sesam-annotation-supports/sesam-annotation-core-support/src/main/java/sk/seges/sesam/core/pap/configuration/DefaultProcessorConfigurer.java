package sk.seges.sesam.core.pap.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import sk.seges.sesam.core.annotation.configuration.Interface;
import sk.seges.sesam.core.annotation.configuration.ProcessorConfiguration;
import sk.seges.sesam.core.pap.configuration.api.ConfigurationElement;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester.AnnotationClassProperty;
import sk.seges.sesam.core.pap.utils.ListUtils;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.core.pap.utils.TypeUtils;

public abstract class DefaultProcessorConfigurer implements ProcessorConfigurer {

	public enum DefaultConfigurationElement implements ConfigurationElement {

		PROCESSING_ANNOTATIONS("annotations") {
			
			@Override
			public boolean hasAnnotationOnField(VariableElement element) {
				return element.getAnnotation(sk.seges.sesam.core.annotation.configuration.Annotation.class) != null;
			}
	
			@Override
			public ElementKind getKind() {
				return ElementKind.CLASS;
			}
			
			@Override
			public boolean isAditive() {
				return true;
			}

			@Override
			public boolean appliesFor(VariableElement field, Element element, ProcessingEnvironment pe) {
				for (AnnotationMirror annotationMirror: element.getAnnotationMirrors()) {
					if (annotationMirror.getAnnotationType().toString().equals(element.asType().toString())) {
						return true;
					}
				}
				return false;
			}
		}, 

		PROCESSING_TYPES("types") {
			
			@Override
			public boolean hasAnnotationOnField(VariableElement element) {
				return element.getAnnotation(sk.seges.sesam.core.annotation.configuration.Type.class) != null;
			}
	
			@Override
			public ElementKind getKind() {
				return ElementKind.CLASS;
			}
			
			@Override
			public boolean isAditive() {
				return true;
			};

			@Override
			public boolean appliesFor(VariableElement field, Element element, ProcessingEnvironment pe) {
				return pe.getTypeUtils().isSameType(pe.getTypeUtils().erasure(field.asType()), pe.getTypeUtils().erasure(element.asType()));
			};
		}, 

		PROCESSING_INTERFACES("interfaces") {
	
			@Override
			public boolean hasAnnotationOnField(VariableElement element) {
				return element.getAnnotation(Interface.class) != null;
			}
	
			@Override
			public ElementKind getKind() {
				return ElementKind.CLASS;
			}
			
			@Override
			public boolean isAditive() {
				return true;
			};

			@Override
			public boolean appliesFor(VariableElement field, Element element, ProcessingEnvironment pe) {
				return ProcessorUtils.implementsType(element.asType(), field.asType());
			};
		};
		
		private String key;
		
		DefaultConfigurationElement(String key) {
			this.key = key;
		}
		
		public String getKey() {
			return key;
		}
		
		public abstract boolean hasAnnotationOnField(VariableElement element);
		public abstract boolean appliesFor(VariableElement field, Element element, ProcessingEnvironment pe);
	}

	class DelayedMessager implements Messager {

		public class MessageInfo {
			Kind kind;
			CharSequence msg;
			Element e;
			AnnotationMirror a;
			AnnotationValue v;
		}
		
		private List<MessageInfo> messages = new ArrayList<MessageInfo>();
		
		@Override
		public void printMessage(Kind kind, CharSequence msg) {
			MessageInfo mi = new MessageInfo();
			mi.kind = kind;
			mi.msg = msg;
			messages.add(mi);
		}

		@Override
		public void printMessage(Kind kind, CharSequence msg, Element e) {
			MessageInfo mi = new MessageInfo();
			mi.kind = kind;
			mi.msg = msg;
			mi.e = e;
			messages.add(mi);
		}

		@Override
		public void printMessage(Kind kind, CharSequence msg, Element e, AnnotationMirror a) {
			MessageInfo mi = new MessageInfo();
			mi.kind = kind;
			mi.msg = msg;
			mi.e = e;
			mi.a = a;
			messages.add(mi);
		}

		@Override
		public void printMessage(Kind kind, CharSequence msg, Element e, AnnotationMirror a, AnnotationValue v) {
			MessageInfo mi = new MessageInfo();
			mi.kind = kind;
			mi.msg = msg;
			mi.e = e;
			mi.a = a;
			mi.v = v;
			messages.add(mi);
		}
		
		public void flush(Messager messager, Element element) {
			for (MessageInfo mi: messages) {
				if (mi.v != null) {
					messager.printMessage(mi.kind, mi.msg, mi.e, mi.a, mi.v);
				} else if (mi.a != null) {
					messager.printMessage(mi.kind, mi.msg, mi.e, mi.a);
				} else {
					messager.printMessage(mi.kind, mi.msg, element);
				}
			}
			messages.clear();
		}
	}
	
	public static final String CONFIG_FILE_LOCATION = "configLocation";
	public static final String PATH_SEPARATOR = "/";

	private static final String CONFIGURATION = "configuration";

	private Map<ConfigurationElement, Set<MutableDeclaredType>> configurationParameters = new HashMap<ConfigurationElement, Set<MutableDeclaredType>>();
	private Map<ConfigurationElement, Set<MutableDeclaredType>> cachedConfiguration = new HashMap<ConfigurationElement, Set<MutableDeclaredType>>();
	private Set<TypeElement> configurations = new HashSet<TypeElement>();
	protected RoundEnvironment roundEnvironment;
	
	protected MutableProcessingEnvironment processingEnv;
	private AbstractProcessor processor;

	private DelayedMessager messager;
	
	protected DefaultProcessorConfigurer() {
		this.messager = new DelayedMessager();
	}
		
	protected String getConfigurationFileName() {
		return null;
	}

	public Messager getMessager() {
		return messager;
	}

	@Override
	public void flushMessages(Messager messager, Element element) {
		this.messager.flush(messager, element);
	}
	
	public void init(MutableProcessingEnvironment processingEnv, AbstractProcessor processor) {
		this.processingEnv = processingEnv;
		this.processor = processor;

		configurationParameters.put(DefaultConfigurationElement.PROCESSING_ANNOTATIONS, parse(new HashSet<MutableDeclaredType>(), processor.getSupportedAnnotationTypes()));
		
		loadConfiguration();
		
		for (DefaultConfigurationElement configurationElement: DefaultConfigurationElement.values()) {
			Type[] configurationElements = getConfigurationElement(configurationElement);
			if (configurationElements != null && configurationElements.length > 0) {
				setConfigurationElement(configurationElement, configurationElements);
			}
		}
	}

	@Override
	public Set<String> getSupportedAnnotations() {
		Type[] annotations = getConfigurationElement(DefaultConfigurationElement.PROCESSING_ANNOTATIONS);
		Set<String> result = new HashSet<String>();
		
		for (Type annotation: annotations) {
			if (annotation instanceof Class) {
				result.add(((Class<?>)annotation).getCanonicalName());
			} else {
				result.add(annotation.toString());
			}
		}

		result.add(ProcessorConfiguration.class.getCanonicalName());

		return result;
	}
	
	private void loadConfiguration() {
		String location = processingEnv.getOptions().get(CONFIG_FILE_LOCATION);
		if (location == null) {
			location = getConfigurationFileLocation();
			if (location == null) {
				return;
			}
		}
				
		Properties prop = new Properties();
		InputStream inputStreamForResource = getInputStreamForResource(location);

		if (inputStreamForResource == null) {
			getMessager().printMessage(Kind.NOTE, "Configuration file not found " + location);
			return;
		}
		try {
			prop.load(inputStreamForResource);
		} catch (IOException e) {
			getMessager().printMessage(Kind.WARNING, "Unable to load properties from " + location + ". " + e.toString());
		}

		for (ConfigurationElement configurationType: DefaultConfigurationElement.values()) {
			String configurationString = (String) prop.get(configurationType.getKey());
			if (configurationString != null) {
				Set<MutableDeclaredType> configurationValues = new HashSet<MutableDeclaredType>();
				parse(configurationValues, configurationString);
				appendConfiguration(configurationType, configurationValues);
			}
		}
		
		String configurationTypeString = (String)prop.get(CONFIGURATION);
		if (configurationTypeString != null) {
			Set<MutableDeclaredType> configurationValues = new HashSet<MutableDeclaredType>();
			parse(configurationValues, configurationTypeString);
			for (MutableDeclaredType configurationType: configurationValues) {
				configurations.add(toTypeElement(configurationType));
			}
		}
		
	}
	
	protected abstract Type[] getConfigurationElement(DefaultConfigurationElement element);
	
	public boolean isSupportedKind(ElementKind kind) {
		return (kind.equals(ElementKind.CLASS) || 
				kind.equals(ElementKind.INTERFACE) ||
				kind.equals(ElementKind.ENUM));
	}

	protected void loadFromConfiguration() {
		Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(ProcessorConfiguration.class);
		
		for (Element element: elementsAnnotatedWith) {
			ProcessorConfiguration configurationAnnotation = element.getAnnotation(ProcessorConfiguration.class);
			TypeElement processorClassType = AnnotationClassPropertyHarvester.getTypeOfClassProperty(configurationAnnotation, new AnnotationClassProperty<ProcessorConfiguration>(){

				@Override
				public Class<?> getClassProperty(ProcessorConfiguration annotation) {
					return annotation.processor();
				}
				
			});
						
			if (this.processor.getClass().getName().equals(processorClassType.getQualifiedName().toString())) {
				configurations.add((TypeElement)element);
			}
		}
		
		for (TypeElement configurationType: configurations) {
			loadFromConfiguration(configurationType);
		}
	}

	@SuppressWarnings("unchecked")
	protected Annotation toAnnotation(String annotationClassName, Element element) {
		try {
			return element.getAnnotation((Class<? extends Annotation>)Class.forName(annotationClassName));
		} catch (ClassNotFoundException e) {
			getMessager().printMessage(Kind.WARNING, "[WARNING] Unable to find annotation " + annotationClassName);
		}
		
		return null;
	}

	protected Annotation toAnnotation(MutableDeclaredType type, Element element) {
		return toAnnotation(type.getCanonicalName(), element);
	}
	
	protected Annotation toAnnotation(AnnotationMirror annotation, Element element) {
		return toAnnotation(annotation.getAnnotationType().toString(), element);
	}

	protected Annotation[] getAnnotations(VariableElement field) {

		List<Annotation> result = new ArrayList<Annotation>();
		
		List<? extends AnnotationMirror> annotationMirrors = field.getAnnotationMirrors();

		MutableDeclaredType[] supportedAnnotations = getMergedConfiguration(DefaultConfigurationElement.PROCESSING_ANNOTATIONS);

		for (AnnotationMirror annotation: annotationMirrors) {
			for (MutableDeclaredType supportedAnnotaion: supportedAnnotations) {
				if (annotation.getAnnotationType().toString().equals(supportedAnnotaion.getCanonicalName())) {
					result.add(toAnnotation(annotation, field));
				}
			}
		}

		return result.toArray(new Annotation[] {});
	}
	
	protected AnnotationMirror[] getAnnotationMirrors(VariableElement field) {

		List<AnnotationMirror> result = new ArrayList<AnnotationMirror>();
		
		List<? extends AnnotationMirror> annotationMirrors = field.getAnnotationMirrors();

		MutableDeclaredType[] supportedAnnotations = getMergedConfiguration(DefaultConfigurationElement.PROCESSING_ANNOTATIONS);

		for (AnnotationMirror annotation: annotationMirrors) {
			for (MutableDeclaredType supportedAnnotaion: supportedAnnotations) {
				if (annotation.getAnnotationType().toString().equals(supportedAnnotaion.getCanonicalName())) {
					result.add(annotation);
				}
			}
		}

		return result.toArray(new AnnotationMirror[] {});
	}
	
	private void loadFromConfiguration(TypeElement typeElement) {
		for (VariableElement field: ElementFilter.fieldsIn(typeElement.getEnclosedElements())) {
			for (ConfigurationElement configurationElement: DefaultConfigurationElement.values()) {
				if (configurationElement.hasAnnotationOnField(field)) {
					
					//This will be always MutableDeclaredType, because it's only over the fields which can be only type (or primitive type)
					MutableDeclaredType namedFieldType = (MutableDeclaredType)processingEnv.getTypeUtils().toMutableType(field.asType());
					
					AnnotationMirror[] annotations = getAnnotationMirrors(field);
					
					for (AnnotationMirror annotation: annotations) {
						namedFieldType.annotateWith(annotation);
					}
					
					appendConfiguration(configurationElement, namedFieldType);
				}
			}
		}
	}

	protected void setConfigurationElement(ConfigurationElement element, Type[] types) {
		overrideConfiguration(element, TypeUtils.mergeClassArray(new Type[] {}, types, processingEnv));
	}
	
	protected Set<MutableDeclaredType> overrideConfiguration(ConfigurationElement configurationElement, MutableDeclaredType[] namedTypes) {
 		Set<MutableDeclaredType> types = configurationParameters.get(configurationElement);
		if (types == null || !configurationElement.isAditive()) {
			types = new HashSet<MutableDeclaredType>();
			configurationParameters.put(configurationElement, types);
		}

		for (MutableDeclaredType namedType: namedTypes) {
			if (!types.contains(namedType)) {
				types.add(namedType);
			}
		}
		
		return types;
	}

	protected Set<MutableDeclaredType> appendConfiguration(ConfigurationElement configurationElement, Set<MutableDeclaredType> namedTypes) {
 		Set<MutableDeclaredType> types = configurationParameters.get(configurationElement);
		if (types == null) {
			types = new HashSet<MutableDeclaredType>();
			configurationParameters.put(configurationElement, types);
		}

		for (MutableDeclaredType namedType: namedTypes) {
			if (!types.contains(namedType)) {
				types.add(namedType);
			}
		}
		
		return types;
	}
	
	protected boolean isSupportedAnnotation(AnnotationMirror annotation) {
		return true;
	}
	
	protected Set<MutableDeclaredType> appendConfiguration(ConfigurationElement configurationElement, MutableDeclaredType namedType) {
		Set<MutableDeclaredType> namedTypes = new HashSet<MutableDeclaredType>();
		namedTypes.add(namedType);
		return appendConfiguration(configurationElement, namedTypes);
	}

	private Set<MutableDeclaredType> ensureConfiguration(ConfigurationElement element) {
		Set<MutableDeclaredType> cachedValue = cachedConfiguration.get(element);
		
		if (cachedValue == null) {
			cachedValue = new HashSet<MutableDeclaredType>();

			for (MutableDeclaredType clazz: getMergedConfiguration(element)) {
				cachedValue.add(clazz);
			}

			cachedConfiguration.put(element, cachedValue);
		}

		return cachedValue;
	}

	protected String getConfigurationFileLocation() {
		return null;
	}
	
	private InputStream getInputStreamForResource(String resource) {
		String pkg = getPackage(resource);
		String name = getRelativeName(resource);
		InputStream ormStream;
		try {
			FileObject fileObject = processingEnv.getFiler().getResource(StandardLocation.CLASS_OUTPUT, pkg, name);
			ormStream = fileObject.openInputStream();
		} catch (IOException e1) {
			// TODO - METAGEN-12
			// unfortunately, the Filer.getResource API seems not to be able to
			// load from /META-INF. One gets a
			// FilerException with the message with "Illegal name /META-INF".
			// This means that we have to revert to
			// using the classpath. This might mean that we find a
			// persistence.xml which is 'part of another jar.
			// Not sure what else we can do here
			ormStream = this.getClass().getResourceAsStream(resource);
		}
		return ormStream;
	}
	
	private String getPackage(String resourceName) {
		if (!resourceName.contains(PATH_SEPARATOR)) {
			return "";
		}
		
		return resourceName.substring(0, resourceName.lastIndexOf(PATH_SEPARATOR));
	}

	private String getRelativeName(String resourceName) {
		if (!resourceName.contains(PATH_SEPARATOR)) {
			return resourceName;
		} 
		
		return resourceName.substring(resourceName.lastIndexOf(PATH_SEPARATOR) + 1);
	}

	private Set<MutableDeclaredType> parse(Set<MutableDeclaredType> result, String line) {
		if (line == null || line.length() == 0) {
			return null;
		}

		StringTokenizer tokenizer = new StringTokenizer(line.trim(), ",");
		String name;
		while (tokenizer.hasMoreElements()) {
			name = tokenizer.nextToken().trim();
			MutableDeclaredType type = processingEnv.getTypeUtils().toMutableType(name);
			if (type == null) {
				if (!name.equals("*")) {
					getMessager().printMessage(Kind.ERROR, "[ERROR] Invalid type (" + name + ") listed in the configuration " + (getConfigurationFileLocation() != null ? getConfigurationFileLocation() : processor.getClass().getCanonicalName()));
				}
			} else {
				result.add(type);
			}
		}
		return result;
	}
	
	private Set<MutableDeclaredType> parse(Set<MutableDeclaredType> result, Set<String> types) {
		if (types == null) {
			return result;
		}
		for (String typeName: types) {
			MutableDeclaredType type = processingEnv.getTypeUtils().toMutableType(typeName);
			if (type == null) {
				if (!typeName.equals("*")) {
					getMessager().printMessage(Kind.ERROR, "[ERROR] Invalid type (" + typeName + ") listed in the configuration " + (getConfigurationFileLocation() != null ? getConfigurationFileLocation() : processor.getClass().getCanonicalName()));
				}
			} else {
				result.add(type);
			}
		}
		return result;
	}

	
	protected Set<MutableDeclaredType> getProcessingAnnotations() {
		return ensureConfiguration(DefaultConfigurationElement.PROCESSING_ANNOTATIONS);
	}
	
	final protected MutableDeclaredType[] getMergedConfiguration(ConfigurationElement element) {
		Set<MutableDeclaredType> configuredTypes = configurationParameters.get(element);
		if (configuredTypes == null) {
			return new MutableDeclaredType[] {};
		}
		return configuredTypes.toArray(new MutableDeclaredType[] {});
	}

	public AnnotationMirror getSupportedAnnotation(MutableDeclaredType type) {
		Set<MutableDeclaredType> annotations = ensureConfiguration(DefaultConfigurationElement.PROCESSING_ANNOTATIONS);
		return getAnnotation(annotations, type);
	}

	public Annotation getSupportedAnnotation(Element element) {
		Set<MutableDeclaredType> annotations = ensureConfiguration(DefaultConfigurationElement.PROCESSING_ANNOTATIONS);
		
		for (MutableDeclaredType annotationType: annotations) {
			//This is ugly hack, but what can we do
			Annotation annotation = toAnnotation(annotationType, element);
			if (annotation != null) {
				return annotation;
			}
		}
		
		for (TypeElement configuration: configurations) {
			List<VariableElement> fields = ElementFilter.fieldsIn(configuration.getEnclosedElements());
			
			for (VariableElement field: fields) {
				
				
				if (processingEnv.getTypeUtils().isSameType(processingEnv.getTypeUtils().erasure(field.asType()), 
														  	processingEnv.getTypeUtils().erasure(element.asType()))) {
					for (DefaultConfigurationElement configurationElement : DefaultConfigurationElement.values()) {
						if (configurationElement.hasAnnotationOnField(field) && configurationElement.appliesFor(field, element, processingEnv)) {
							Annotation[] result = getAnnotations(field);
							if (result != null && result.length > 0) {
								return result[0];
							}
						}
					}
				}
			}
		}
		
		return null;
	}
	
	public AnnotationMirror getSupportedAnnotationMirror(Element element) {
		Set<MutableDeclaredType> annotations = ensureConfiguration(DefaultConfigurationElement.PROCESSING_ANNOTATIONS);
		return getAnnotation(annotations, element);
	}

	protected AnnotationMirror getAnnotation(Collection<MutableDeclaredType> annotationTypes, Collection<? extends AnnotationMirror> annotationMirrors) {
		if (annotationMirrors == null) {
			return null;
		}
		for (AnnotationMirror mirror : annotationMirrors) {
			Element annotationElement = mirror.getAnnotationType().asElement();
			MutableTypeMirror annotationType = processingEnv.getTypeUtils().toMutableType(annotationElement.asType());
			if (annotationTypes.contains(annotationType)) {
				return mirror;
			}
		}
		return null;
	}
	
	protected AnnotationMirror getAnnotation(Collection<MutableDeclaredType> annotationTypes, MutableDeclaredType type) {
		if (annotationTypes == null || annotationTypes.size() == 0) {
			return null;
		}

		return getAnnotation(annotationTypes, type.getAnnotations());
	}
	
	protected AnnotationMirror getAnnotation(Collection<MutableDeclaredType> annotationTypes, Element element) {
		if (annotationTypes == null || annotationTypes.size() == 0) {
			return null;
		}

		return getAnnotation(annotationTypes, element.getAnnotationMirrors());
	}

	public boolean isSupportedByInterface(TypeElement typeElement) {
		if (ensureConfiguration(DefaultConfigurationElement.PROCESSING_INTERFACES).size() == 0) {
			return false;
		}

		for (MutableDeclaredType type : ensureConfiguration(DefaultConfigurationElement.PROCESSING_INTERFACES)) {
			if (ProcessorUtils.isAssignableFrom(typeElement, type)) {
				return true;
			}
		}

		return false;
	}

	protected Set<Element> getConfiguredProcessingTypes() {
		Set<Element> result = new HashSet<Element>();
		MutableDeclaredType[] configuredTypes = getMergedConfiguration(DefaultConfigurationElement.PROCESSING_TYPES);
		for (MutableDeclaredType configuredType: configuredTypes) {
			AnnotationMirror supportedAnnotation = getSupportedAnnotation(configuredType);
			if (supportedAnnotation == null || (supportedAnnotation != null && isSupportedAnnotation(supportedAnnotation))) {
				result.add(toTypeElement(configuredType));
			}
		}
		return result;
	}
	
	@Override
	public Set<Element> getElements(RoundEnvironment roundEnvironment) {
		this.roundEnvironment = roundEnvironment;
		
		loadFromConfiguration();
		
		Set<Element> processingElements = new HashSet<Element>();
		
		for (Element element : roundEnvironment.getRootElements()) {
			if (element.getAnnotation(ProcessorConfiguration.class) == null && !ListUtils.contains(processingElements, element)) {
				TypeElement typeElement = (TypeElement) element;
				if (isSupportedByInterface(typeElement)) {
					processingElements.add(element);
				} else {
					AnnotationMirror supportedAnnotation = getSupportedAnnotationMirror(typeElement);
					if (supportedAnnotation != null && isSupportedAnnotation(supportedAnnotation) && isSupportedKind(element.getKind())) {
						processingElements.add(element);
					}
				}
			}
		}

		Set<MutableDeclaredType> annotations = getProcessingAnnotations();
		
		if (annotations != null) {
			for (MutableDeclaredType annotationType: annotations) {
				
				TypeElement typeElement = processingEnv.getElementUtils().getTypeElement(annotationType.getQualifiedName());
				
				if (typeElement != null) {
					
					Set<? extends Element> els = roundEnvironment.getElementsAnnotatedWith(typeElement);
					for (Element element : els) {
						if (isSupportedKind(element.getKind()) && element.getAnnotation(ProcessorConfiguration.class) == null && !ListUtils.contains(processingElements, element)) {
							List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
							for (AnnotationMirror annotationMirror: annotationMirrors) {
								if (isSupportedAnnotation(annotationMirror)) {
									processingElements.add(element);
								}
							}
						}
					}
				} else {
					processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Invalid annotation definition " + annotationType.getQualifiedName());
				}
			}
		}

		for (Element element: getConfiguredProcessingTypes()) {
			//Element is not in the list, element is not configuration and element is supported type
			if (!ListUtils.contains(processingElements, element) && element.getAnnotation(ProcessorConfiguration.class) == null  && isSupportedKind(element.getKind())) {
				processingElements.add(element);
			}
		}
		
		return processingElements;
	}


	public AnnotationValue getAnnotationValueByReturnType(Class<?> returnType, AnnotationMirror annotationMirror) {
		return getAnnotationValueByReturnType(toTypeElement(returnType), annotationMirror);
	}
	
	public AnnotationValue getAnnotationValueByReturnType(TypeElement returnType, AnnotationMirror annotationMirror) {
		Iterator<ExecutableElement> iterator = ElementFilter.methodsIn(
				processingEnv.getTypeUtils().asElement(annotationMirror.getAnnotationType()).getEnclosedElements()).iterator();
		while (iterator.hasNext()) {
			ExecutableElement executableElement = iterator.next();
			if (executableElement.getReturnType() != null) {
				Element returnElement = processingEnv.getTypeUtils().asElement(executableElement.getReturnType());
				if (processingEnv.getElementUtils().getBinaryName((TypeElement)returnElement).toString().equals(processingEnv.getElementUtils().getBinaryName(returnType).toString())) {
					return annotationMirror.getElementValues().get(executableElement);
				}
			}
		}
		return null;
	}	

	/** Helper methods **/
	protected TypeElement toTypeElement(Class<?> clazz) {
		return processingEnv.getElementUtils().getTypeElement(clazz.getCanonicalName());
	}

	protected TypeElement toTypeElement(MutableDeclaredType type) {
		return processingEnv.getElementUtils().getTypeElement(type.getCanonicalName());
	}
}