package sk.seges.sesam.core.pap.processor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import sk.seges.sesam.core.annotation.configuration.ProcessorConfiguration;
import sk.seges.sesam.core.pap.builder.ClassPathTypeUtils;
import sk.seges.sesam.core.pap.builder.api.ClassPathTypes;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.utils.ListUtils;

public abstract class ConfigurableAnnotationProcessor extends PlugableAnnotationProcessor {

	public static final String SUPPORTED_PACKAGE = "sk.seges";
	
	public static final String PROJECT_NAME_OPTION = "projectName";
	public static final String CLASSPATH_OPTION = "classpath";
	public static final String TEST_CLASSPATH_OPTION = "testClasspath";

	protected ProcessorConfigurer configurer;
	protected RoundEnvironment roundEnv;

	private Set<Element> processedElement = new HashSet<Element>();

	protected ConfigurableAnnotationProcessor() {
		configurer = getConfigurer();
	}

	protected abstract ProcessorConfigurer getConfigurer();

    @Override
	public Set<String> getSupportedOptions() {
    	SupportedOptions so = this.getClass().getAnnotation(SupportedOptions.class);
    	Set<String> result = new HashSet<String>();
    	if  (so != null) {
    	    result = arrayToSet(so.value());
    	}
    	
    	result.add(CLASSPATH_OPTION);
    	result.add(PROJECT_NAME_OPTION);
    	result.add(TEST_CLASSPATH_OPTION);
    	
    	return result;
    }

    protected static Set<String> arrayToSet(String[] array) {
		assert array != null;
		Set<String> set = new HashSet<String>(array.length);
		for (String s : array) {
			set.add(s);
		}
		return Collections.unmodifiableSet(set);
    }

    protected ClassPathTypes getClassPathTypes() {
	    String projectName = processingEnv.getOptions().get(PROJECT_NAME_OPTION);
	    
	    if (projectName == null) {
	    	projectName = getClass().getCanonicalName();
	    }

		return new ClassPathTypeUtils(processingEnv, projectName, SUPPORTED_PACKAGE);
    }

	@Override
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

	@Override
	public synchronized void init(ProcessingEnvironment pe) {
		super.init(pe);
		this.configurer.init(processingEnv, this);
	}
	
	protected boolean supportProcessorChain() {
		//Return true in order to run other processors
		return true;
	}

	@Override
	public final boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
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
					if (configurer == null || configurer.isSupportedKind(element.getKind())) {
						init(element, roundEnv);
						processElement(element, roundEnv);
						configurer.flushMessages(processingEnv.getMessager(), element);
					}
				}
			}
		}

		return !supportProcessorChain();
	}

	protected void init(Element element, RoundEnvironment roundEnv) {};
	
	protected abstract boolean processElement(Element element, RoundEnvironment roundEnv);
}