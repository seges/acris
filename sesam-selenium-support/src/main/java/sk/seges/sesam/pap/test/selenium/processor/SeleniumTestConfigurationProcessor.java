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
import sk.seges.sesam.core.pap.model.api.MutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.test.selenium.annotation.SeleniumTest;
import sk.seges.sesam.core.test.selenium.annotation.SeleniumTestConfiguration;
import sk.seges.sesam.core.test.selenium.configuration.DefaultBromineEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.DefaultSeleniumEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.DefaultTestEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.api.Browsers;
import sk.seges.sesam.core.test.selenium.configuration.api.TestEnvironment;
import sk.seges.sesam.core.test.selenium.runner.SeleniumSuiteRunner;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class SeleniumTestConfigurationProcessor extends AbstractConfigurableProcessor {

	protected ElementKind getElementKind() {
		return ElementKind.CLASS;
	}

	@Override
	protected Type[] getConfigurationTypes(DefaultConfigurationType type, TypeElement typeElement) {

		switch (type) {
			case OUTPUT_SUPERCLASS:
				return new Type[] {
						SeleniumSuiteRunner.class
				};
		}
		
		return super.getConfigurationTypes(type, typeElement);
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> annotations = new HashSet<String>();
		annotations.add(SeleniumTestConfiguration.class.getCanonicalName());
		return annotations;
	}
	
	public static final MutableType getOutputClass(MutableType mutableType) {
		return mutableType.addClassSufix("Configuration");
	}
	
	@Override
	protected Type[] getImports() {

		List<Type> result = new ArrayList<Type>();
		Iterator<? extends Element> iterator = seleniumTestClasses.iterator();

		NameTypesUtils nameTypesUtils = new NameTypesUtils(processingEnv.getElementUtils(), processingEnv.getTypeUtils());
		
		while (iterator.hasNext()) {
			result.add(SeleniumTestProcessor.getOutputClass(nameTypesUtils.toType(((TypeElement)iterator.next()))));
		}
		
		result.add(TestEnvironment.class);
		result.add(DefaultSeleniumEnvironment.class);
		result.add(DefaultBromineEnvironment.class);
		result.add(DefaultTestEnvironment.class);
		result.add(Browsers.class);
		
		return result.toArray(new Type[] {});
	}
	
	@Override
	protected NamedType[] getTargetClassNames(MutableType mutableType) {
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

		pw.println("public void run(" + TestEnvironment.class.getSimpleName() + " testEnvironment) throws " + Exception.class.getSimpleName() +"{");
		
		NameTypesUtils nameTypesUtils = new NameTypesUtils(processingEnv.getElementUtils(), processingEnv.getTypeUtils());
		
		for (Element seleniumTestClass: seleniumTestClasses) {
			pw.println("run(new " + SeleniumTestProcessor.getOutputClass(nameTypesUtils.toType(((TypeElement)seleniumTestClass))).getSimpleName() + "(testEnvironment));");
		}
		
		pw.println("}");
		pw.println("");

		SeleniumTestConfiguration seleniumTestConfiguration = element.getAnnotation(SeleniumTestConfiguration.class);

		pw.println("public static " + TestEnvironment.class.getSimpleName() + " getTestConfiguration() {");
		pw.println(DefaultTestEnvironment.class.getSimpleName() + " defaultTestEnvironment = null;");
		
		if (seleniumTestConfiguration != null) {
			pw.println(DefaultSeleniumEnvironment.class.getSimpleName() + " defaultSeleniumEnvironment = new " + DefaultSeleniumEnvironment.class.getSimpleName() + 
					"(\"" + NullCheck.checkNull(seleniumTestConfiguration.seleniumServer()) + "\"," + seleniumTestConfiguration.seleniumPort() + ");");
			pw.println(DefaultBromineEnvironment.class.getSimpleName() + " defaultBromineEnvironment = new " + DefaultBromineEnvironment.class.getSimpleName() + 
					"(\"" + NullCheck.checkNull(seleniumTestConfiguration.bromineServer()) + "\"," + seleniumTestConfiguration.brominePort() + ");");
			pw.println("defaultTestEnvironment = new " + DefaultTestEnvironment.class.getSimpleName() + 
					"(defaultSeleniumEnvironment, defaultBromineEnvironment, \"" + NullCheck.checkNull(seleniumTestConfiguration.testURL()) + "\", " +
					Browsers.class.getSimpleName() + "." + seleniumTestConfiguration.browser().name() + ");");
		}
		pw.println("return defaultTestEnvironment;");
		pw.println("}");
		pw.println("");
		
		pw.println("public void testAll() throws " + Exception.class.getSimpleName() + "{");
		pw.println("run(mergeConfiguration(getTestConfiguration()));");
		pw.println("}");
	}
}