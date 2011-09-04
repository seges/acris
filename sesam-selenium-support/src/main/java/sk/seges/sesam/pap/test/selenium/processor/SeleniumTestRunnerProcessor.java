package sk.seges.sesam.pap.test.selenium.processor;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.builder.NameTypesUtils;
import sk.seges.sesam.core.pap.configuration.api.OutputDefinition;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.test.selenium.configuration.DefaultBromineEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.DefaultSeleniumEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.DefaultTestEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumSuite;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumTest;
import sk.seges.sesam.core.test.selenium.configuration.api.Browsers;
import sk.seges.sesam.core.test.selenium.configuration.api.TestEnvironment;
import sk.seges.sesam.core.test.selenium.runner.SeleniumSuiteRunner;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class SeleniumTestRunnerProcessor extends AbstractConfigurableProcessor {

	protected ElementKind getElementKind() {
		return ElementKind.CLASS;
	}

	@Override
	protected Type[] getOutputDefinition(OutputDefinition type, TypeElement typeElement) {
		switch (type) {
			case OUTPUT_SUPERCLASS:
				return new Type[] {
						SeleniumSuiteRunner.class
				};
		}
		return super.getOutputDefinition(type, typeElement);
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> annotations = new HashSet<String>();
		annotations.add(SeleniumSuite.class.getCanonicalName());
		return annotations;
	}
	
	public static final ImmutableType getOutputClass(ImmutableType mutableType) {
		return mutableType.addClassSufix("Configuration");
	}
	
	@Override
	protected Type[] getImports() {

		List<Type> result = new ArrayList<Type>();
		Iterator<? extends Element> iterator = seleniumTestClasses.iterator();

		NameTypesUtils nameTypesUtils = new NameTypesUtils(processingEnv);
		
		while (iterator.hasNext()) {
			result.add(SeleniumTestProcessor.getOutputClass((ImmutableType)nameTypesUtils.toType(((TypeElement)iterator.next()))));
		}
		
		result.add(TestEnvironment.class);
		result.add(DefaultSeleniumEnvironment.class);
		result.add(DefaultBromineEnvironment.class);
		result.add(DefaultTestEnvironment.class);
		result.add(Browsers.class);
		
		return result.toArray(new Type[] {});
	}
	
	@Override
	protected NamedType[] getTargetClassNames(ImmutableType mutableType) {
		return new NamedType[] {
			getOutputClass(mutableType)
		};
	};

	private Set<? extends Element> seleniumTestClasses = new HashSet<Element>();
	
	@Override
	protected boolean processElement(Element element, RoundEnvironment roundEnv) {
		if (!roundEnv.processingOver()) {
			seleniumTestClasses = roundEnv.getElementsAnnotatedWith(SeleniumTest.class);
		}

		return super.processElement(element, roundEnv);
	}

	@Override
	protected void processElement(TypeElement element, NamedType outputClass, RoundEnvironment roundEnv, PrintWriter pw) {

		pw.println("public void run() {");
		
		NameTypesUtils nameTypesUtils = new NameTypesUtils(processingEnv);
		
		for (Element seleniumTestClass: seleniumTestClasses) {
			pw.println("run(new " + SeleniumTestProcessor.getOutputClass((ImmutableType) nameTypesUtils.toType(((TypeElement)seleniumTestClass))).getSimpleName() + "());");
		}
		
		pw.println("}");
		pw.println("");
	}
}