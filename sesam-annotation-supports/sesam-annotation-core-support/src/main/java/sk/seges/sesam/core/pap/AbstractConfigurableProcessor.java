package sk.seges.sesam.core.pap;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Generated;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import sk.seges.sesam.core.annotation.configuration.ProcessorConfiguration;
import sk.seges.sesam.core.pap.api.SubProcessor;
import sk.seges.sesam.core.pap.builder.ClassPathTypeUtils;
import sk.seges.sesam.core.pap.builder.NameTypeUtils;
import sk.seges.sesam.core.pap.builder.api.ClassPathTypes;
import sk.seges.sesam.core.pap.builder.api.NameTypes;
import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.core.pap.configuration.api.OutputDefinition;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.TypedClassBuilder;
import sk.seges.sesam.core.pap.model.api.ArrayNamedType;
import sk.seges.sesam.core.pap.model.api.HasTypeParameters;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.model.api.TypeParameter;
import sk.seges.sesam.core.pap.model.api.TypeVariable;
import sk.seges.sesam.core.pap.utils.ListUtils;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.utils.TypeParametersSupport;
import sk.seges.sesam.core.pap.utils.TypeUtils;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

public abstract class AbstractConfigurableProcessor extends AbstractProcessor {

	private Map<String, Set<SubProcessor<?>>> subProcessors = new HashMap<String, Set<SubProcessor<?>>>();

	protected RoundEnvironment roundEnv;
	protected TypeParametersSupport typeParametersSupport;
	protected MethodHelper methodHelper;
	protected NameTypeUtils nameTypesUtils;
	protected ProcessorConfigurer configurer;
	private Set<Element> processedElement = new HashSet<Element>();
	private Map<OutputDefinition, Set<NamedType>> cachedDefinition = new HashMap<OutputDefinition, Set<NamedType>>();

	private final String lineSeparator;

	protected AbstractConfigurableProcessor() {
		configurer = getConfigurer();
		lineSeparator = (String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"));
	}
	
	protected boolean hasSubProcessor(TypeElement element) {
		return subProcessors.get(element.getQualifiedName().toString()) != null;
	}

	private void addImport(List<? extends Type> imports, NamedType namedType) {
		if (namedType.getPackageName() != null && !namedType.getPackageName().equals(Void.class.getPackage().getName())) {
			ListUtils.addUnique(imports, namedType);
		}
	}

	protected Type[] getSubProcessorImports(TypeElement typeElement) {
		List<Type> types = new ArrayList<Type>();
		for (Set<SubProcessor<?>> subProcessorSet : subProcessors.values()) {
			for (SubProcessor<?> subProcessor: subProcessorSet) {
				ListUtils.add(types, subProcessor.getImports(typeElement));
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
	
	public static final String PROJECT_NAME_OPTION = "projectName";
	public static final String CLASSPATH_OPTION = "classpath";

    public Set<String> getSupportedOptions() {
    	SupportedOptions so = this.getClass().getAnnotation(SupportedOptions.class);
    	Set<String> result = new HashSet<String>();
    	if  (so != null) {
    	    result = arrayToSet(so.value());
    	}
    	
    	result.add(CLASSPATH_OPTION);
    	result.add(PROJECT_NAME_OPTION);
    	
    	return result;
    }
    
    protected static Set<String> arrayToSet(String[] array) {
		assert array != null;
		Set<String> set = new HashSet<String>(array.length);
		for (String s : array)
		    set.add(s);
		return Collections.unmodifiableSet(set);
    }

    protected ClassPathTypes getClassPathTypes() {
	    String projectName = processingEnv.getOptions().get(PROJECT_NAME_OPTION);
	    
	    if (projectName == null) {
	    	projectName = getClass().getCanonicalName();
	    }

		return new ClassPathTypeUtils(processingEnv, projectName);
    }
    
	public Set<String> getSupportedAnnotationTypes() {

	    SupportedAnnotationTypes sat = this.getClass().getAnnotation(SupportedAnnotationTypes.class);

		Set<String> result = null;
		
		if (sat != null) {
			result = new HashSet<String>(super.getSupportedAnnotationTypes());
		} else {
			result = new HashSet<String>();
		}
		
		if (configurer != null) {
			result.addAll(configurer.getSupportedAnnotations());
		}

		if (result.size() == 0) {
		    processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "No SupportedAnnotationTypes for " + this.getClass().getName() + ", returning an empty set.");
		}

		return result;
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

	//String location = processingEnv.getOptions().get(CONFIG_FILE_LOCATION);

	@Override
	public synchronized void init(ProcessingEnvironment pe) {
		super.init(pe);
		
		this.nameTypesUtils = new NameTypeUtils(processingEnv);

		typeParametersSupport = new TypeParametersSupport(pe, nameTypesUtils);

		this.methodHelper = new MethodHelper(pe, nameTypesUtils);

		if (this.configurer != null) {
			this.configurer.init(pe, this);
		}
	}

	protected ProcessorConfigurer getConfigurer() {
		return null;
	}

	protected void addGenericType(List<NamedType> result, NamedType importName, TypeElement typeElement) {
		if (typeParametersSupport.hasTypeParameters(importName)) {
			for (TypeParameter typeParameter: ((HasTypeParameters)importName).getTypeParameters()) {
				if (typeParameter.getBounds() != null) {
					for (TypeVariable typeVariable: typeParameter.getBounds()) {
						if (typeVariable.getUpperBound() != null) {
							NamedType type = getNameTypes().toType(typeVariable.getUpperBound());
							addImport(result, type);
							addGenericType(result, type, typeElement);
						}
					}
				}
			}
		} else if (importName instanceof ArrayNamedType) {
			addImport(result, ((ArrayNamedType)importName).getComponentType());
			addGenericType(result, ((ArrayNamedType)importName).getComponentType(), typeElement);
		}
	}

	protected Type[] getOutputDefinition(OutputDefinition type, TypeElement typeElement) {
		return new Type[] {
		};
	}

	final protected NamedType[] getMergedDefinition(OutputDefinition type, TypeElement typeElement) {
		return TypeUtils.mergeClassArray(new Type[] {}, getOutputDefinition(type, typeElement), nameTypesUtils);
	}

	protected NamedType[] getAllImports(TypeElement typeElement) {
		List<NamedType> imports = new ArrayList<NamedType>();

		ListUtils.add(imports, TypeUtils.toTypes(getImports(), nameTypesUtils));
		ListUtils.add(imports, TypeUtils.toTypes(getImports(typeElement), nameTypesUtils));
		ListUtils.add(imports, getMergedDefinition(OutputDefinition.OUTPUT_SUPERCLASS, typeElement));
		ListUtils.add(imports, getMergedDefinition(OutputDefinition.OUTPUT_INTERFACES, typeElement));

		List<NamedType> result = new ArrayList<NamedType>();
		
		for (NamedType importName: imports) {
			addImport(result, importName);
			addGenericType(result, importName, typeElement);
		}
		
		return result.toArray(new NamedType[] {});
	}

	protected ElementKind getElementKind() {
		return ElementKind.CLASS;
	}
	
	protected Type[] getImports(TypeElement typeElement) {
		return new Type[] { };
	}

	protected Type[] getImports() {
		return new Type[] { };
	}

	protected NameTypes getNameTypes() {
		return nameTypesUtils;
	}
	
	protected final NamedType[] getClassNames(Element element) {
		return getTargetClassNames((ImmutableType)getNameTypes().toType(element));
	}

	protected abstract NamedType[] getTargetClassNames(ImmutableType mutableType);

	protected FormattedPrintWriter initializePrintWriter(OutputStream os) {
		FormattedPrintWriter pw = new FormattedPrintWriter(os, processingEnv);
		pw.setAutoIndent(true);
		pw.setSerializer(ClassSerializer.SIMPLE);
		pw.serializeTypeParameters(true);
		return pw;
	}

	protected boolean isSupportedKind(ElementKind kind) {
		return (kind.equals(ElementKind.CLASS) || 
				kind.equals(ElementKind.INTERFACE) ||
				kind.equals(ElementKind.ENUM));
	}
	
	protected boolean isSupportedAnnotation(AnnotationMirror annotation) {
		return true;
	}
		
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		this.roundEnv = roundEnv;
		if (!roundEnv.processingOver()) {
			Set<Element> processingElements = null;

			if (configurer != null) {
				processingElements = configurer.getElements(roundEnv);
			} else {
				processingElements = new HashSet<Element>();

				for (String annotationType: getSupportedAnnotationTypes()) {
					if (!annotationType.equals(ProcessorConfiguration.class.getCanonicalName())) {
						TypeElement typeElement = processingEnv.getElementUtils().getTypeElement(annotationType);
						if (typeElement != null) {
							Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(typeElement);
							for (Element element: elementsAnnotatedWith) {
								processingElements.add(element);
							}
						}
					}
				}
			}
			
			for (Element element: processingElements) {
				if (!ListUtils.contains(processedElement, element)) {
					processedElement.add(element);
					if (isSupportedKind(element.getKind())) {
						processElement(element, roundEnv);

						cachedDefinition = new HashMap<OutputDefinition, Set<NamedType>>();
						if (configurer != null) {
							configurer.flushMessages(processingEnv.getMessager(), element);
						}
					}
				}
			}
		}

		return !supportProcessorChain();
	}

	protected void writeClassAnnotations(Element el, NamedType outputName, PrintWriter pw) {}
	
	protected void writeClassAnnotations(Element el, NamedType outputName, FormattedPrintWriter pw) {
		writeClassAnnotations(el, outputName, (PrintWriter)pw);
	}

	protected boolean checkPreconditions(Element element, NamedType outputName, boolean alreadyExists) {
		return true;
	}
	
	protected boolean processElement(Element element, RoundEnvironment roundEnv) {

		PrintWriter pw = null;
		
		TypeElement typeElement = (TypeElement) element;

		NamedType[] outputNames = getClassNames(element);
		
		processingEnv.getMessager().printMessage(Kind.NOTE, "Processing " + element.getSimpleName().toString() + " with " + getClass().getSimpleName(), element);

		for (NamedType outputName: outputNames) {

			boolean alreadyExists = processingEnv.getElementUtils().getTypeElement(outputName.getCanonicalName()) != null;

			if (!checkPreconditions(element, outputName, alreadyExists)) {
				if (alreadyExists) {
					processingEnv.getMessager().printMessage(Kind.NOTE, "[INFO] File " + outputName.getCanonicalName() + " already exists.", element);
				}
				processingEnv.getMessager().printMessage(Kind.NOTE, "[INFO] Skipping file " + outputName.getCanonicalName() + " processing.", element);
				continue;
			}
			
			try {
				JavaFileObject createSourceFile = processingEnv.getFiler().createSourceFile(outputName.getCanonicalName(), element);
				OutputStream os = createSourceFile.openOutputStream();
				pw = initializePrintWriter(os);
				pw.println("package " + outputName.getPackageName() + ";");
				pw.println();
	
				NamedType[] imports = getAllImports(typeElement);

				ByteArrayOutputStream annotationsOutputStream = new ByteArrayOutputStream();
				FormattedPrintWriter annotationsPrintWriter = initializePrintWriter(annotationsOutputStream);
				writeClassAnnotations(element, outputName, annotationsPrintWriter);
				annotationsPrintWriter.flush();

				ByteArrayOutputStream contentOutputStream = new ByteArrayOutputStream();
				FormattedPrintWriter contentPrintWriter = initializePrintWriter(contentOutputStream);
				contentPrintWriter.setDefaultIdentLevel(1);
				processElement(typeElement, outputName, roundEnv, contentPrintWriter);
				contentPrintWriter.flush();
				
				List<NamedType> mergedImports = new ArrayList<NamedType>();

				ListUtils.add(mergedImports, imports);
				ListUtils.addUnique(mergedImports, annotationsPrintWriter.getUsedTypes());
				ListUtils.addUnique(mergedImports, contentPrintWriter.getUsedTypes());
				ListUtils.addUnique(mergedImports, ListUtils.add(new ArrayList<NamedType>(), new NamedType[] { nameTypesUtils.toType(Generated.class)}));

				mergedImports = removeNoPackageImports(mergedImports);

				List<NamedType> collectedImports = new ArrayList<NamedType>();
				
				for (NamedType importName: mergedImports) {
					addImport(collectedImports, importName);
					addGenericType(collectedImports, importName, typeElement);
				}

				sortByPackage(collectedImports);
				
				String previousPackage = null;
				
				for (NamedType importType : collectedImports) {
					if (previousPackage != null && !getVeryTopPackage(importType).equals(previousPackage)) {
						pw.println();
					}
					//TODO do no print types that are nested in the output class
					pw.println("import " + importType.toString(ClassSerializer.CANONICAL, false) + ";");
					previousPackage = getVeryTopPackage(importType);
				}

				pw.println();
				pw.println(toString(annotationsOutputStream));
				pw.println("@" + Generated.class.getSimpleName() + "(value = \"" + this.getClass().getCanonicalName() + "\")");
				
				Set<NamedType> superClassTypes = ensureOutputDefinition(OutputDefinition.OUTPUT_SUPERCLASS, typeElement);
				NamedType superClass = null;

				if ((getElementKind().equals(ElementKind.CLASS) || getElementKind().equals(ElementKind.INTERFACE))
						&& superClassTypes.size() == 1) {
					superClass = superClassTypes.iterator().next();
					
					if (typeParametersSupport.hasVariableParameterTypes(superClass) && !typeParametersSupport.hasTypeParameters(outputName)) {
						outputName = TypedClassBuilder.get(outputName, typeParametersSupport.getVariableParameterTypes((HasTypeParameters)superClass));
					}
				}
				
				pw.print("public " + getElementKind().name().toLowerCase() + " " + outputName.toString(ClassSerializer.SIMPLE, true));
				
				if ((getElementKind().equals(ElementKind.CLASS) || getElementKind().equals(ElementKind.INTERFACE))
						&& superClass != null) {
					
					superClass = typeParametersSupport.stripTypesFromTypeParameters(superClass);
					pw.print(" extends " + superClass.toString(ClassSerializer.SIMPLE, true));
				}
	
				if (ensureOutputDefinition(OutputDefinition.OUTPUT_INTERFACES, typeElement).size() > 0) {
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
						for (NamedType type : ensureOutputDefinition(OutputDefinition.OUTPUT_INTERFACES, typeElement)) {
							if (i > 0) {
								pw.print(", ");
							}
							pw.print(type.toString(ClassSerializer.SIMPLE, true));
							i++;
						}
					}
				}
				pw.println(" {");
				pw.println();
				//processElement(typeElement, outputName, roundEnv, pw);
				pw.println(toString(contentOutputStream));
				pw.println("}");
				pw.flush();
				
			} catch (Exception e) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to process element " + e.getMessage(), element);
			} finally {
				if (pw != null) {
					pw.close();
				}
			}

		}
		
		return supportProcessorChain();
	}

	private String getVeryTopPackage(NamedType importType) {
		String importPackage = importType.getPackageName();
		int index = importPackage.indexOf('.');
		if (index != -1) {
			importPackage = importPackage.substring(0, index);
		}
		return importPackage;
	}
	
	private List<NamedType> removeNoPackageImports(List<NamedType> imports) {
		List<NamedType> result = new ArrayList<NamedType>();
		for (NamedType importType: imports) {
			if (importType.getPackageName() != null && importType.getPackageName().length() > 0)  {
				result.add(importType);
			}
		}
		
		return result;
	}
	
	private void sortByPackage(List<NamedType> imports) {
		Collections.sort(imports, new Comparator<NamedType>() {

			@Override
			public int compare(NamedType o1, NamedType o2) {
				return o1.getPackageName().compareTo(o2.getPackageName());
			}
			
		});
	}
	
	public String toString(ByteArrayOutputStream outputStream) {
		String s = outputStream.toString();
		if (s.endsWith(lineSeparator)) {
			s = s.substring(0, s.length() - lineSeparator.length());
		}
		int i = 0;
		while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
			i++;
		}
		return s.substring(i);
	}
	
	/**
	 * This method should be overrided
	 */
	protected void processElement(TypeElement typeElement, NamedType outputName, RoundEnvironment roundEnv, PrintWriter pw) {}

	protected void processElement(TypeElement typeElement, NamedType outputName, RoundEnvironment roundEnv, FormattedPrintWriter pw) {
		processElement(typeElement, outputName, roundEnv, (PrintWriter)pw);
	}
	
	protected boolean supportProcessorChain() {
		//Return true in order to run other processors
		return true;
	}
	
	//TODO move to the helper class
	@SuppressWarnings("unchecked")
	protected static <T extends Annotation> T hasAnnotation(List<NamedType> types, TypeElement typeElement, Elements elements, NameTypes nameTypes) {
		if (types == null || types.size() == 0) {
			return null;
		}

		List<? extends AnnotationMirror> annotationMirrors = typeElement.getAnnotationMirrors();
		for (AnnotationMirror mirror : annotationMirrors) {
			NamedType inputClass = nameTypes.toType(mirror.getAnnotationType());
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

	protected TypeElement toTypeElement(Class<?> clazz) {
		return processingEnv.getElementUtils().getTypeElement(clazz.getCanonicalName());
	}

	private Set<NamedType> ensureOutputDefinition(OutputDefinition type, TypeElement typeElement) {
		Set<NamedType> cachedValue = cachedDefinition.get(type);
		
		if (cachedValue == null) {
			cachedValue = new HashSet<NamedType>();

			for (NamedType clazz: getMergedDefinition(type, typeElement)) {
				cachedValue.add(clazz);
			}

			cachedDefinition.put(type, cachedValue);
		}

		return cachedValue;
	}
}