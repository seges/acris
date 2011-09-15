package sk.seges.sesam.pap.test.selenium.processor.model;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester;
import sk.seges.sesam.core.pap.utils.AnnotationClassPropertyHarvester.AnnotationClassProperty;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumTest;

public class SeleniumTestTypeElement extends AbstractSeleniumTypeElement {

	private final TypeElement testCase;
	
	private boolean testSuiteInitialized = false;
	private SeleniumSuiteTypeElement testSuite;
	
	private boolean configurationInitialized = false;
	private SeleniumTestConfigurationTypeElement configuration;
	
	public SeleniumTestTypeElement(TypeElement testCase, ProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.testCase = testCase;
	}

	@Override
	protected ImmutableType getDelegateImmutableType() {
		return getNameTypeUtils().toImmutableType(testCase);
	}
	
	public SeleniumSuiteTypeElement getSeleniumSuite() {
		if (!testSuiteInitialized) {
			SeleniumTest seleniumTestAnnotation = testCase.getAnnotation(SeleniumTest.class);
			
			if (seleniumTestAnnotation != null) {
				TypeElement seleniumSuiteElement = AnnotationClassPropertyHarvester.getTypeOfClassProperty(seleniumTestAnnotation, new AnnotationClassProperty<SeleniumTest>() {

					@Override
					public Class<?> getClassProperty(SeleniumTest annotation) {
						return annotation.suiteRunner();
					}
					
				});
				
				if (seleniumSuiteElement != null) {
					testSuite = new SeleniumSuiteTypeElement(seleniumSuiteElement, processingEnv);
				}
			}
			
			testSuiteInitialized = true;
		}
		
		return testSuite;
	}

	public AnnotationMirror getAnnotationMirror(AnnotationMirror annotationMirror) {
		for (AnnotationMirror testAnnotationMirror: testCase.getAnnotationMirrors()) {
			if (testAnnotationMirror.getAnnotationType().equals(annotationMirror.getAnnotationType())) {
				return testAnnotationMirror;
			}
		}
		
		return null;
	}
	
	public TypeElement asElement() {
		return testCase;
	}
	
	public SeleniumTestConfigurationTypeElement getConfiguration() {
		if (!configurationInitialized) {
			configuration = new SeleniumTestConfigurationTypeElement(this, processingEnv);
			configurationInitialized = true;
		}
		
		return configuration;
	}
}