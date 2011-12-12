package sk.seges.sesam.pap.test.selenium.processor.model;

import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.test.selenium.processor.accessor.SeleniumTestCaseAccessor;

public class SeleniumTestCaseType extends AbstractSeleniumType {

	private final TypeElement testCase;
	
	private boolean testSuiteInitialized = false;
	private List<SeleniumSuiteType> testSuites;

	private boolean configurationInitialized = false;
	private TypeElement configuration;

	private boolean settingsInitialized = false;
	private SeleniumTestSettingsType settings;
	
	public SeleniumTestCaseType(TypeElement testCase, MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.testCase = testCase;
	}

	@Override
	protected MutableDeclaredType getDelegate() {
		return (MutableDeclaredType) getMutableTypeUtils().toMutableType(testCase.asType());
	}
	
	public List<SeleniumSuiteType> getSeleniumSuites() {
		if (!testSuiteInitialized) {
			testSuites = new SeleniumTestCaseAccessor(testCase, processingEnv).getSeleniumSuites();
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

	public TypeElement getConfiguration() {
		if (!configurationInitialized) {
			configuration = new SeleniumTestCaseAccessor(testCase, processingEnv).getConfiguration();
			configurationInitialized = true;
		}
		
		return configuration;
	}

	public SeleniumTestSettingsType getSettings() {
		if (!settingsInitialized) {
			settings = new SeleniumTestSettingsType(this, processingEnv);
			settingsInitialized = true;
		}
		
		return settings;
	}
}