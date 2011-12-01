package sk.seges.sesam.pap.test.selenium.processor.model;

import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.test.selenium.processor.accessor.SeleniumTestAccessor;

public class SeleniumTestTypeElement extends AbstractSeleniumTypeElement {

	private final TypeElement testCase;
	
	private boolean testSuiteInitialized = false;
	private List<SeleniumSuiteTypeElement> testSuites;
	
	private boolean configurationInitialized = false;
	private SeleniumTestConfigurationTypeElement configuration;
	
	public SeleniumTestTypeElement(TypeElement testCase, MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.testCase = testCase;
	}

	@Override
	protected MutableDeclaredType getDelegate() {
		return (MutableDeclaredType) getMutableTypeUtils().toMutableType(testCase.asType());
	}
	
	public List<SeleniumSuiteTypeElement> getSeleniumSuites() {
		if (!testSuiteInitialized) {
			testSuites = new SeleniumTestAccessor(testCase, processingEnv).getSeleniumSuites();
			testSuiteInitialized = true;
		}
		
		return testSuites;
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