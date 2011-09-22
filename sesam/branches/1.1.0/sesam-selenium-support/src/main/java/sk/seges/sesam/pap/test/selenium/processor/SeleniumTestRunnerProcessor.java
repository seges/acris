package sk.seges.sesam.pap.test.selenium.processor;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

import org.junit.Test;

import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.configuration.api.OutputDefinition;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumSuite;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumTest;
import sk.seges.sesam.core.test.selenium.runner.SeleniumSuiteRunner;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class SeleniumTestRunnerProcessor extends AbstractConfigurableProcessor {

	private static final String SUFFIX = "Runner";

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
	protected NamedType[] getTargetClassNames(ImmutableType mutableType) {
		return new NamedType[] {
			getOutputClass(mutableType)
		};
	};

	public static final ImmutableType getOutputClass(ImmutableType mutableType) {
		return mutableType.addClassSufix(SUFFIX);
	}	

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> annotations = new HashSet<String>();
		annotations.add(SeleniumSuite.class.getCanonicalName());
		return annotations;
	}
		
	@Override
	protected void processElement(TypeElement element, NamedType outputClass, RoundEnvironment roundEnv, FormattedPrintWriter pw) {

		pw.println("public void run() {");
		
		Set<? extends Element> seleniumTestClasses = getClassPathTypes().getElementsAnnotatedWith(SeleniumTest.class);

		for (Element seleniumTestClass: seleniumTestClasses) {

			List<ExecutableElement> methods = ElementFilter.methodsIn(seleniumTestClass.getEnclosedElements());

			for (ExecutableElement method: methods) {
				Test annotation = method.getAnnotation(Test.class);
				if (annotation != null) {
					pw.println("{");
					
					String testName = MethodHelper.toField(seleniumTestClass.getSimpleName().toString());
					
					pw.println(seleniumTestClass, " " + testName + " = new ", seleniumTestClass, "();");
					//TODO find before annotation
					pw.println(testName, ".setUp();");
					pw.println(testName, "." + method.getSimpleName().toString() + "();");
					//TODO find after annotation
					pw.println(testName, ".tearDown();");
					pw.println("}");
				}
			}
		}
		
		pw.println("}");
		pw.println("");
	}
}