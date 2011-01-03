package sk.seges.sesam.core.pap;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

import sk.seges.sesam.core.pap.model.InputClass;
import sk.seges.sesam.core.pap.model.InputClass.HasTypeParameters;
import sk.seges.sesam.core.pap.model.InputClass.OutputClass;
import sk.seges.sesam.core.pap.model.TypedClass;
import sk.seges.sesam.core.pap.model.TypedClass.TypeParameter;
import sk.seges.sesam.core.pap.model.api.Type;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

public abstract class AbstractConfigurableProcessor extends AbstractProcessor {

	public static final String CONFIG_FILE_LOCATION = "configLocation";
	protected static final String PATH_SEPARATOR = "/";

	protected enum ConfigurationType {
		PROCESSING_ANNOTATIONS("annotations"), 
		PROCESSING_INTERFACES("interfaces"), 
		
		OUTPUT_INHERITORS("inheritors"), 
		OUTPUT_SUPERCLASS("superClass");
		
		private String key;
		
		ConfigurationType(String key) {
			this.key = key;
		}
		
		public String getKey() {
			return key;
		}
	}

	public static final Class<?> THIS = TypedClass.class;

	private Map<ConfigurationType, Set<String>> configurationParameters = new HashMap<ConfigurationType, Set<String>>();
	
	protected Set<Element> elements = new HashSet<Element>();

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
		try {
			prop.load(getInputStreamForResource(location));
		} catch (IOException e) {
			throw new RuntimeException("Unable to initialize.", e);
		}

		for (ConfigurationType configurationType: ConfigurationType.values()) {
			String configurationString = (String) prop.get(configurationType.getKey());
			if (configurationString != null) {
				Set<String> configurationValues = new HashSet<String>();
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

	protected <T> List<T> addUnique(List<T> source, List<T> additions) {
		if (additions != null) {
			for (T addClass : additions) {
				addUnique(source, addClass);
			}
		}
		return source;
	}

	protected <T> List<T> addUnique(List<T> source, T[] additions) {
		if (additions != null) {
			for (T addClass : additions) {
				addUnique(source, addClass);
			}
		}
		return source;
	}

	protected AnnotatedElement[] getAllImports() {
		List<AnnotatedElement> imports = new ArrayList<AnnotatedElement>();

		addUnique(imports, getImports());
		addUnique(imports, getMergedConfiguration(ConfigurationType.OUTPUT_SUPERCLASS));
		addUnique(imports, getMergedConfiguration(ConfigurationType.OUTPUT_INHERITORS));

		return imports.toArray(new AnnotatedElement[] {});
	}

	protected ElementKind getElementKind() {
		return ElementKind.CLASS;
	}
	
	protected AnnotatedElement[] getImports() {
		return new AnnotatedElement[] { };
	}

	protected final OutputClass[] getClassNames(Element element) {

		String packageName = processingEnv.getElementUtils().getPackageOf(element).getQualifiedName().toString();
		String simpleName = element.getSimpleName().toString();

		return getTargetClassNames(new InputClass(packageName, simpleName));
	}

	protected abstract OutputClass[] getTargetClassNames(InputClass inputClass);

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
	
	private AnnotatedElement[] mergeClassArray(AnnotatedElement[] classes, Set<String> classNames) {
		List<AnnotatedElement> result = new ArrayList<AnnotatedElement>();

		addUnique(result, classes);

		if (classNames != null) {
			for (String annotationString: classNames) {
				try {
					result.add((Class<?>)Class.forName(annotationString));
				} catch (ClassNotFoundException e) {
					processingEnv.getMessager().printMessage(Kind.WARNING, "Unable to find annotation " + annotationString + " on the classpath");
				}
			}
		}
		
		return result.toArray(new AnnotatedElement[] {});		
	}
	
	protected AnnotatedElement[] getConfigurationTypes(ConfigurationType type) {
		return new AnnotatedElement[] {
		};
	}

	final protected AnnotatedElement[] getMergedConfiguration(ConfigurationType type) {
		return mergeClassArray(getConfigurationTypes(type), configurationParameters.get(ConfigurationType.PROCESSING_INTERFACES));
	}

	protected boolean isSupportedAnnotation(Annotation annotation) {
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.annotation.processing.AbstractProcessor#process(java.util.Set,
	 * javax.annotation.processing.RoundEnvironment)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		if (roundEnv.processingOver()) {
			// finally process gathered elements and create java files
			for (Element element : elements) {
				processElement(element, roundEnv);
			}
		} else {
			for (AnnotatedElement annotation : getMergedConfiguration(ConfigurationType.PROCESSING_ANNOTATIONS)) {
				Class<Annotation> annotationClass = null;
				if (annotation instanceof Class) {
					annotationClass = (Class<Annotation>)annotation;
				}
				
				if (annotationClass != null) {
					Set<? extends Element> els = roundEnv.getElementsAnnotatedWith(annotationClass);
					for (Element element : els) {
						if (isSupportedKind(element.getKind())) {
							if (isSupportedAnnotation(element.getAnnotation(annotationClass))) {
								elements.add(element);
							}
						}
					}
				} else {
					processingEnv.getMessager().printMessage(Kind.ERROR, "Invalid annotation definition " + annotation.toString());
				}
			}
			for (Element element : roundEnv.getRootElements()) {
				if (isSupportedKind(element.getKind())) {
					TypeElement typeElement = (TypeElement) element;
					if (isAssignable(typeElement)) {
						elements.add(element);
					}
				}
			}
		}

		return !supportProcessorChain();
	}

	protected void writeClassAnnotations(PrintWriter pw, Element el) {}

	enum ClassSerializer {
		CANONICAL, SIMPLE, QUALIFIED;
	}

	private String toString(TypeParameter typeParameter, InputClass inputClass, ClassSerializer serializer) {
		String result = "";
		
		if (typeParameter.getVariable() != null) {
			result += typeParameter.getVariable() + " ";
		}
		
		if (typeParameter.getBounds() != null) {
			if (typeParameter.getVariable() != null) {
				result += "extends ";
			}
			if (typeParameter.getBounds().equals(THIS)) {
				result += toString(inputClass, serializer);
			} else {
				result += toString(inputClass, typeParameter.getBounds(), serializer, true);
			}
		}
		
		if (result.length() == 0) {
			throw new IllegalArgumentException("Invalid type parameter");
		}
		return result;
	}

	private String toString(Type clazz, ClassSerializer serializer) {
		switch (serializer) {
		case CANONICAL:
		case QUALIFIED:
			return clazz.getCanonicalName();
		case SIMPLE:
			return clazz.getSimpleName();
		}
		return null;
	}

	private String toString(Class<?> clazz, ClassSerializer serializer) {
		switch (serializer) {
		case CANONICAL:
			return clazz.getCanonicalName();
		case QUALIFIED:
			return clazz.getName();
		case SIMPLE:
			return clazz.getSimpleName();
		}
		return null;
	}
	
	private String getCanonicalName(InputClass inputClass, AnnotatedElement annotatedElement, boolean typed) {
		return toString(inputClass, annotatedElement, ClassSerializer.CANONICAL, typed);
	}

	private String getSimpleName(InputClass inputClass, AnnotatedElement annotatedElement, boolean typed) {
		return toString(inputClass, annotatedElement, ClassSerializer.SIMPLE, typed);
	}

	private String getQualifiedName(InputClass inputClass, AnnotatedElement annotatedElement, boolean typed) {
		return toString(inputClass, annotatedElement, ClassSerializer.QUALIFIED, typed);
	}

	private String toString(InputClass inputClass, AnnotatedElement annotatedElement, ClassSerializer serializer, boolean typed) {
		if (annotatedElement instanceof java.lang.reflect.Type) {
			return toString(inputClass, (java.lang.reflect.Type)annotatedElement, serializer, typed);
		}
		throw new IllegalArgumentException("Not supported annotated element " + annotatedElement.toString());
	}
	
	private String toString(InputClass inputClass, java.lang.reflect.Type type, ClassSerializer serializer, boolean typed) {
		if (type instanceof Class) {
			return toString((Class<?>)type, serializer);
		}
		
		if (type instanceof HasTypeParameters) {
			HasTypeParameters hasTypeClass = (HasTypeParameters)type;
			
			String resultName = toString(hasTypeClass, serializer);
			
			if (!typed || hasTypeClass.getTypeParameters() == null || hasTypeClass.getTypeParameters().length == 0) {
				return resultName;
			}
			
			String types = "<";
			
			int i = 0;
			
			for (TypeParameter typeParameter: hasTypeClass.getTypeParameters()) {
				if (i > 0) {
					types += ", ";
				}
				types += toString(typeParameter, inputClass, ClassSerializer.CANONICAL);
				i++;
			}
			
			types += ">";
			
			return resultName + types;
		}
		
		if (type instanceof Type) {
			return toString((Type)type, serializer);
		}
		
		throw new IllegalArgumentException("Not supported annotation element " + type.toString());
	}
	
	protected boolean processElement(Element element, RoundEnvironment roundEnv) {

		PrintWriter pw = null;

		TypeElement typeElement = (TypeElement) element;

		OutputClass[] outputNames = getClassNames(element);
		
		for (OutputClass outputName: outputNames) {
			try {
				JavaFileObject createSourceFile = processingEnv.getFiler().createSourceFile(outputName.getCanonicalName(), element);
				OutputStream os = createSourceFile.openOutputStream();
				pw = initializePrintWriter(os);
				pw.println("package " + outputName.getPackageName() + ";");
				pw.println();
	
				for (AnnotatedElement importClass : getAllImports()) {
					pw.println("import " + getCanonicalName(outputName, importClass, false) + ";");
				}
	
				pw.println();
				
				writeClassAnnotations(pw, element);

				pw.print("public " + getElementKind().name().toLowerCase() + " " + toString(outputName, outputName, ClassSerializer.SIMPLE, true));
	
				if (getElementKind().equals(ElementKind.CLASS) && getMergedConfiguration(ConfigurationType.OUTPUT_SUPERCLASS).length == 1) {
					pw.print(" extends " + getSimpleName(outputName, getMergedConfiguration(ConfigurationType.OUTPUT_SUPERCLASS)[0], true));
				}
	
				if (getMergedConfiguration(ConfigurationType.OUTPUT_INHERITORS).length > 0) {
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
						for (AnnotatedElement interfaceClass : getMergedConfiguration(ConfigurationType.OUTPUT_INHERITORS)) {
							if (i > 0) {
								pw.print(", ");
							}
							pw.print(getSimpleName(outputName, interfaceClass, true));
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
	protected void processElement(TypeElement element, OutputClass outputName, RoundEnvironment roundEnv, PrintWriter pw) {	}
	
	protected boolean supportProcessorChain() {
		//Return true in order to run other processors
		return true;
	}
	
	private Map<ConfigurationType, List<String>> cachedConfiguration = new HashMap<ConfigurationType, List<String>>();
	
	private List<String> ensureConfiguration(ConfigurationType type) {
		List<String> cachedValue = cachedConfiguration.get(type);
		
		if (cachedValue == null) {
			cachedValue = new ArrayList<String>();
			
			for (AnnotatedElement clazz: getMergedConfiguration(type)) {
				cachedValue.add(getQualifiedName(null, clazz, false));
			}

			cachedConfiguration.put(type, cachedValue);
		}
		return cachedValue;
	}
	
	protected boolean hasAnnotation(TypeElement typeElement) {
		if (ensureConfiguration(ConfigurationType.PROCESSING_ANNOTATIONS).size() == 0) {
			return false;
		}

		List<? extends AnnotationMirror> annotationMirrors = typeElement.getAnnotationMirrors();
		for (AnnotationMirror mirror : annotationMirrors) {
			String mirrorFqn = ((TypeElement) mirror.getAnnotationType().asElement()).getQualifiedName().toString();
			if (ensureConfiguration(ConfigurationType.PROCESSING_ANNOTATIONS).contains(mirrorFqn)) {
				return true;
			}
		}
		return false;
	}

	private boolean isAssignable(TypeElement typeElement) {
		if (ensureConfiguration(ConfigurationType.PROCESSING_INTERFACES).size() == 0) {
			return false;
		}

		for (String iface : ensureConfiguration(ConfigurationType.PROCESSING_INTERFACES)) {
			if (ProcessorUtils.isAssignableFrom(typeElement, iface)) {
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

	private Set<String> parse(Set<String> result, String line) {
		if (line == null || line.length() == 0) {
			return null;
		}

		StringTokenizer tokenizer = new StringTokenizer(line.trim(), ",");
		String name;
		while (tokenizer.hasMoreElements()) {
			name = tokenizer.nextToken().trim();
			result.add(name);
		}
		return result;
	}
}