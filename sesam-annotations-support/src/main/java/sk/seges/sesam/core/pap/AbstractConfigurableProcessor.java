package sk.seges.sesam.core.pap;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
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
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

import sk.seges.sesam.core.pap.api.SubProcessor;
import sk.seges.sesam.core.pap.builder.NameTypesUtils;
import sk.seges.sesam.core.pap.builder.api.NameTypes;
import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.core.pap.model.TypeParameterBuilder;
import sk.seges.sesam.core.pap.model.TypedClassBuilder;
import sk.seges.sesam.core.pap.model.api.HasTypeParameters;
import sk.seges.sesam.core.pap.model.api.MutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.model.api.TypeParameter;
import sk.seges.sesam.core.pap.model.api.TypeVariable;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

public abstract class AbstractConfigurableProcessor extends AbstractProcessor {

	public static final String CONFIG_FILE_LOCATION = "configLocation";
	protected static final String PATH_SEPARATOR = "/";

	protected interface ConfigurationType {

		String getKey();
		
		boolean isAditive();
		
		ElementKind getKind();
	}
	
	protected enum DefaultConfigurationType implements ConfigurationType {
		PROCESSING_ANNOTATIONS("annotations", true, ElementKind.CLASS), 
		PROCESSING_INTERFACES("interfaces", true, ElementKind.CLASS), 
		
		OUTPUT_INTERFACES("inheritors", true, ElementKind.CLASS), 
		OUTPUT_SUPERCLASS("superClass", true, ElementKind.CLASS);
		
		private String key;
		private boolean aditive;
		private ElementKind kind;
		
		DefaultConfigurationType(String key, boolean aditive, ElementKind kind) {
			this.key = key;
			this.aditive = aditive;
			this.kind = kind;
		}
		
		@Override
		public boolean isAditive() {
			return aditive;
		}
		
		@Override
		public ElementKind getKind() {
			return kind;
		}
				
		public String getKey() {
			return key;
		}
	}

	private Map<ConfigurationType, Set<NamedType>> configurationParameters = new HashMap<ConfigurationType, Set<NamedType>>();
	private Map<String, Set<SubProcessor<?>>> subProcessors = new HashMap<String, Set<SubProcessor<?>>>();
	
	protected boolean hasSubProcessor(TypeElement element) {
		return subProcessors.get(element.getQualifiedName().toString()) != null;
	}

	protected Type applyUpperGenerics(Type type, TypeElement typeElement) {
		if (typeElement.getTypeParameters() != null && typeElement.getTypeParameters().size() > 0) {
			TypeParameter[] variables = new TypeParameter[typeElement.getTypeParameters().size()];
			int i = 0;
			for (TypeParameterElement typeParameterElement: typeElement.getTypeParameters()) {
				if (typeParameterElement.getBounds() != null && typeParameterElement.getBounds().size() == 1) {
					Element element = processingEnv.getTypeUtils().asElement(typeParameterElement.getBounds().get(0));
					if (element.getKind().equals(ElementKind.CLASS) ||
						element.getKind().equals(ElementKind.INTERFACE)) {
						TypeElement boundType = (TypeElement)element;
						variables[i] = TypeParameterBuilder.get(boundType.toString());
					} else {
						variables[i] = TypeParameterBuilder.get("?");
					}
				} else {
					variables[i] = TypeParameterBuilder.get("?");
				}
				i++;
			}
			return TypedClassBuilder.get(type, variables);
		}
		
		return type;
	}
	
	protected Type applyVariableGenerics(Type type, TypeElement typeElement) {
		if (typeElement.getTypeParameters() != null && typeElement.getTypeParameters().size() > 0) {
			TypeParameter[] variables = new TypeParameter[typeElement.getTypeParameters().size()];
			int i = 0;
			for (TypeParameterElement typeParameterElement: typeElement.getTypeParameters()) {
				if (typeParameterElement.asType().getKind().equals(TypeKind.TYPEVAR)) {
					variables[i] = TypeParameterBuilder.get(typeParameterElement.toString());
				} else {
					variables[i] = TypeParameterBuilder.get("?");
				}
				i++;
			}
			return TypedClassBuilder.get(type, variables);
		}
		
		return type;
	}
	
	protected Type[] getSubProcessorImports() {
		List<Type> types = new ArrayList<Type>();
		for (Set<SubProcessor<?>> subProcessorSet : subProcessors.values()) {
			for (SubProcessor<?> subProcessor: subProcessorSet) {
				addUnique(types, subProcessor.getImports());
			}
		}
		return types.toArray(new Type[] {});
	}
	
	protected void initSubProcessors(ProcessingEnvironment processingEnv) {
		for (Set<SubProcessor<?>> subProcessorSet : subProcessors.values()) {
			for (SubProcessor<?> subProcessor: subProcessorSet) {
				subProcessor.init(processingEnv);
			}
		}
	}

	protected boolean processSubProcessor(PrintWriter printWriter, NamedType outputClass, TypeElement element, TypeElement subElement) {
		Set<SubProcessor<?>> subProcessorSet = subProcessors.get(subElement.getQualifiedName().toString());

		boolean result = true;
		
		for (SubProcessor<?> subProcessor: subProcessorSet) {
			result |= subProcessor.process(printWriter, outputClass, element, subElement);
		}
		
		return result;
	}

	protected void registerSubProcessor(Class<?> clazz, SubProcessor<?> subProcessor) {
		Set<SubProcessor<?>> processors = subProcessors.get(clazz.getName());

		if (processors == null) {
			processors = new HashSet<SubProcessor<?>>();
			subProcessors.put(clazz.getName(), processors);
		}
		
		processors.add(subProcessor);
	}

	@Override
	public synchronized void init(ProcessingEnvironment pe) {
		super.init(pe);

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
			processingEnv.getMessager().printMessage(Kind.NOTE, "Configuration file not found " + location);
			return;
		}
		try {
			prop.load(inputStreamForResource);
		} catch (IOException e) {
			throw new RuntimeException("Unable to initialize.", e);
		}

		for (ConfigurationType configurationType: DefaultConfigurationType.values()) {
			String configurationString = (String) prop.get(configurationType.getKey());
			if (configurationString != null) {
				Set<NamedType> configurationValues = new HashSet<NamedType>();
				parse(configurationValues, configurationString);
				configurationParameters.put(configurationType, configurationValues);
			}
		}
	}

	protected String getConfigurationFileLocation() {
		return null;
	}

	protected <T> List<T> addUnique(List<T> source, T t) {
		if (t == null) {
			return source;
		}
		for (T s : source) {
			if (s.equals(t)) {
				return source;
			}
		}

		source.add(t);
		return source;
	}

	protected <T> List<T> addUnique(List<T> source, Collection<T> additions) {
		if (additions != null) {
			for (T addClass : additions) {
				addUnique(source, addClass);
			}
		}
		return source;
	}

	protected <T extends Type> List<T> addUnique(List<T> source, T[] additions) {
		if (additions != null) {
			for (T addClass : additions) {
				addUnique(source, addClass);
			}
		}
		return source;
	}

//	protected void stripGenerics(MutableType mutableType) {
//		mutableType
//	}
	
	protected void addGenericType(List<NamedType> result, NamedType importName, TypeElement typeElement) {
		if (importName.getQualifiedName().equals(NamedType.THIS.getName())) {
			importName = getNameTypes().toType(typeElement);
		}
		if (importName instanceof HasTypeParameters) {
			for (TypeParameter typeParameter: ((HasTypeParameters)importName).getTypeParameters()) {
				for (TypeVariable typeVariable: typeParameter.getBounds()) {
					if (typeVariable.getUpperBound() != null) {
						NamedType type = getNameTypes().toType(typeVariable.getUpperBound());
						result.add(type);
						addGenericType(result, type, typeElement);
					}
				}
			}
		}
	}

	protected NamedType[] getAllImports(TypeElement typeElement) {
		List<NamedType> imports = new ArrayList<NamedType>();

		addUnique(imports, toTypes(getImports()));
		addUnique(imports, getMergedConfiguration(DefaultConfigurationType.OUTPUT_SUPERCLASS, typeElement));
		addUnique(imports, getMergedConfiguration(DefaultConfigurationType.OUTPUT_INTERFACES, typeElement));

		List<NamedType> result = new ArrayList<NamedType>();
		
		for (NamedType importName: imports) {
			result.add(importName);
			addGenericType(result, importName, typeElement);
		}
		
		return result.toArray(new NamedType[] {});
	}

	protected ElementKind getElementKind() {
		return ElementKind.CLASS;
	}
	
	protected Type[] getImports() {
		return new Type[] { };
	}

	protected NameTypes getNameTypes() {
		return new NameTypesUtils(processingEnv.getElementUtils(), processingEnv.getTypeUtils());
	}
	
	protected final NamedType[] getClassNames(Element element) {
		return getTargetClassNames(getNameTypes().toType(element));
	}

	protected abstract NamedType[] getTargetClassNames(MutableType mutableType);

	protected PrintWriter initializePrintWriter(OutputStream os) {
		FormattedPrintWriter pw = new FormattedPrintWriter(os);
		pw.setAutoIndent(true);
		return pw;
	}

	protected boolean isSupportedKind(ElementKind kind) {
		return (kind.equals(ElementKind.CLASS) || 
				kind.equals(ElementKind.INTERFACE) ||
				kind.equals(ElementKind.ENUM));
	}
	
	private NamedType[] mergeClassArray(Type[] classes, Set<NamedType> classNames) {
		List<NamedType> result = new ArrayList<NamedType>();

		addUnique(result, toTypes(classes));
		addUnique(result, classNames);
		
		return result.toArray(new NamedType[] {});		
	}
	
	protected Type[] getConfigurationTypes(ConfigurationType type, TypeElement typeElement) {
		if (type instanceof DefaultConfigurationType) {
			return getConfigurationTypes((DefaultConfigurationType)type, typeElement);
		}
		return new Type[] {
		};
	}

	protected Type[] getConfigurationTypes(DefaultConfigurationType type, TypeElement typeElement) {
		return new Type[] {
		};
	}

	final protected NamedType[] getMergedConfiguration(ConfigurationType type, TypeElement typeElement) {
		return mergeClassArray(getConfigurationTypes(type, typeElement), configurationParameters.get(type));
	}

	protected boolean isSupportedAnnotation(AnnotationMirror annotation) {
		return true;
	}
	
	protected NamedType[] getProcessingAnnotations(ConfigurationType type) {
		NamedType[] mergeClassArray = mergeClassArray(getConfigurationTypes(type, null), parse(new HashSet<NamedType>(), getSupportedAnnotationTypes()));
		return mergeClassArray(mergeClassArray, configurationParameters.get(type));
	}
	
	private Set<Element> processedElement = new HashSet<Element>();
	
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		if (!roundEnv.processingOver()) {
			Set<Element> processingElements = new HashSet<Element>();
			for (NamedType annotationType : getProcessingAnnotations(DefaultConfigurationType.PROCESSING_ANNOTATIONS)) {
					TypeElement typeElement = processingEnv.getElementUtils().getTypeElement(annotationType.getQualifiedName());
					
					if (typeElement != null) {
						
						Set<? extends Element> els = roundEnv.getElementsAnnotatedWith(typeElement);
						for (Element element : els) {
							if (isSupportedKind(element.getKind())) {
								List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
								for (AnnotationMirror annotationMirror: annotationMirrors) {
									if (isSupportedAnnotation(annotationMirror)) {
										processingElements.add(element);
									}
								}
							}
						}
					} else {
						processingEnv.getMessager().printMessage(Kind.ERROR, "Invalid annotation definition " + annotationType.getQualifiedName());
					}
			}
			for (Element element : roundEnv.getRootElements()) {
				if (isSupportedKind(element.getKind())) {
					TypeElement typeElement = (TypeElement) element;
					if (isAssignable(typeElement)) {
						processingElements.add(element);
					}
				}
			}
			
			for (Element element: processingElements) {
				if (!processedElement.contains(element)) {
					processedElement.add(element);
					processElement(element, roundEnv);
				}
			}
		}

		return !supportProcessorChain();
	}

	protected void writeClassAnnotations(PrintWriter pw, Element el) {}
	
	protected boolean processElement(Element element, RoundEnvironment roundEnv) {

		PrintWriter pw = null;

		TypeElement typeElement = (TypeElement) element;

		NamedType[] outputNames = getClassNames(element);
		NamedType inputClass = getNameTypes().toType(typeElement);
		
		for (NamedType outputName: outputNames) {
			try {
				JavaFileObject createSourceFile = processingEnv.getFiler().createSourceFile(outputName.getCanonicalName(), element);
				OutputStream os = createSourceFile.openOutputStream();
				pw = initializePrintWriter(os);
				pw.println("package " + outputName.getPackageName() + ";");
				pw.println();
	
				for (NamedType importType : getAllImports(typeElement)) {
					pw.println("import " + importType.toString(inputClass, ClassSerializer.CANONICAL, false) + ";");
				}
	
				pw.println();
				
				writeClassAnnotations(pw, element);

				pw.print("public " + getElementKind().name().toLowerCase() + " " + outputName.toString(inputClass, ClassSerializer.SIMPLE, true));
				
				NamedType[] superClassTypes = getMergedConfiguration(DefaultConfigurationType.OUTPUT_SUPERCLASS, typeElement);
				
				if (getElementKind().equals(ElementKind.CLASS) && superClassTypes.length == 1) {
					pw.print(" extends " + superClassTypes[0].toString(inputClass, ClassSerializer.SIMPLE, true));
				}
	
				if (getMergedConfiguration(DefaultConfigurationType.OUTPUT_INTERFACES, typeElement).length > 0) {
					boolean supportedType = false;
					
					if (getElementKind().equals(ElementKind.CLASS)) {
						pw.print(" implements ");
						supportedType = true;
					} else 	if (getElementKind().equals(ElementKind.INTERFACE)) {
						pw.print(" extends ");
						supportedType = true;
					}
	
					if (supportedType) {
						int i = 0;
						for (NamedType type : getMergedConfiguration(DefaultConfigurationType.OUTPUT_INTERFACES, typeElement)) {
							if (i > 0) {
								pw.print(", ");
							}
							pw.print(type.toString(inputClass, ClassSerializer.SIMPLE, true));
							i++;
						}
					}
				}
				pw.println(" {");
				pw.println();
				processElement(typeElement, outputName, roundEnv, pw);
				pw.println("}");
				pw.flush();
	
			} catch (Exception e) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "Unable to process element " + e.getMessage(), element);
			} finally {
				if (pw != null) {
					pw.close();
				}
			}
		}
		
		return supportProcessorChain();
	}

	/**
	 * This method should be overrided
	 */
	protected void processElement(TypeElement element, NamedType outputName, RoundEnvironment roundEnv, PrintWriter pw) {	}
	
	protected boolean supportProcessorChain() {
		//Return true in order to run other processors
		return true;
	}
	
	private Map<ConfigurationType, List<NamedType>> cachedConfiguration = new HashMap<ConfigurationType, List<NamedType>>();

	private List<NamedType> toTypes(Type[] javaTypes) {
		List<NamedType> result = new ArrayList<NamedType>();
		for (Type javaType: javaTypes) {
			result.add(getNameTypes().toType(javaType));
		}
		return result;
	}
	
	private List<NamedType> ensureConfiguration(ConfigurationType type, TypeElement typeElement) {
		List<NamedType> cachedValue = cachedConfiguration.get(type);
		
		if (cachedValue == null) {
			cachedValue = new ArrayList<NamedType>();
			
			for (NamedType clazz: getMergedConfiguration(type, typeElement)) {
				cachedValue.add(clazz);
			}

			cachedConfiguration.put(type, cachedValue);
		}
		return cachedValue;
	}

	@SuppressWarnings("unchecked")
	protected static <T extends Annotation> T hasAnnotation(List<NamedType> types, TypeElement typeElement, Elements elements, NameTypes nameTypes) {
		if (types == null || types.size() == 0) {
			return null;
		}

		List<? extends AnnotationMirror> annotationMirrors = typeElement.getAnnotationMirrors();
		for (AnnotationMirror mirror : annotationMirrors) {
			Element element = mirror.getAnnotationType().asElement();
			NamedType inputClass = nameTypes.toType(element);
			if (types.contains(inputClass)) {
				try {
					Class<Annotation> annotationClass = (Class<Annotation>) Class.forName(inputClass.getQualifiedName());
				
					if (annotationClass != null) {
						return (T) typeElement.getAnnotation(annotationClass);
					}
				} catch (ClassNotFoundException e) {
					return null;
				}
			}
		}
		return null;
	}

	
	@SuppressWarnings("unchecked")
	protected <T extends Annotation> T hasAnnotation(TypeElement typeElement) {
		List<NamedType> configuration = ensureConfiguration(DefaultConfigurationType.PROCESSING_ANNOTATIONS, typeElement);
		
		Annotation annotation = hasAnnotation(configuration, typeElement, processingEnv.getElementUtils(), getNameTypes());
		return (T)annotation;
	}

	private boolean isAssignable(TypeElement typeElement) {
		if (ensureConfiguration(DefaultConfigurationType.PROCESSING_INTERFACES, typeElement).size() == 0) {
			return false;
		}

		for (NamedType type : ensureConfiguration(DefaultConfigurationType.PROCESSING_INTERFACES, typeElement)) {
			if (ProcessorUtils.isAssignableFrom(typeElement, type)) {
				return true;
			}
		}

		return false;
	}

	protected void log(String msg) {
		processingEnv.getMessager().printMessage(Kind.MANDATORY_WARNING, msg);
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
		} else {
			return resourceName.substring(0, resourceName.lastIndexOf(PATH_SEPARATOR));
		}
	}

	private String getRelativeName(String resourceName) {
		if (!resourceName.contains(PATH_SEPARATOR)) {
			return resourceName;
		} else {
			return resourceName.substring(resourceName.lastIndexOf(PATH_SEPARATOR) + 1);
		}
	}

	protected AnnotationValue getAnnotationValueByReturnType(TypeElement returnType, TypeElement annotationType, AnnotationMirror annotationMirror) {
		Iterator<ExecutableElement> iterator = ElementFilter.methodsIn(annotationType.getEnclosedElements()).iterator();
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
	
	private Set<NamedType> parse(Set<NamedType> result, Set<String> types) {
		if (types == null) {
			return result;
		}
		for (String type: types) {
			result.add(getNameTypes().toType(type));
		}
		return result;
	}
	
	
	private Set<NamedType> parse(Set<NamedType> result, String line) {
		if (line == null || line.length() == 0) {
			return null;
		}

		StringTokenizer tokenizer = new StringTokenizer(line.trim(), ",");
		String name;
		while (tokenizer.hasMoreElements()) {
			name = tokenizer.nextToken().trim();
			result.add(getNameTypes().toType(name));
		}
		return result;
	}
	
	protected TypeElement toTypeElement(Class<?> clazz) {
		return processingEnv.getElementUtils().getTypeElement(clazz.getCanonicalName());
	}

}