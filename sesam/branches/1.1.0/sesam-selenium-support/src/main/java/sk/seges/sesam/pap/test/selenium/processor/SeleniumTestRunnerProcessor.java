package sk.seges.sesam.pap.test.selenium.processor;

import java.util.List;
import java.util.Set;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.util.ElementFilter;

import org.junit.Test;

import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.processor.MutableAnnotationProcessor;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumTest;
import sk.seges.sesam.pap.test.selenium.processor.configurer.SeleniumSuiteProcessorConfigurer;
import sk.seges.sesam.pap.test.selenium.processor.model.SeleniumSuiteTypeElement;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class SeleniumTestRunnerProcessor extends MutableAnnotationProcessor {

	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] {
			new SeleniumSuiteTypeElement(context.getTypeElement(), processingEnv).getSuiteRunner()
		};
	}

	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new SeleniumSuiteProcessorConfigurer();
	}
		
	@Override
	protected void processElement(ProcessorContext context) {

		FormattedPrintWriter pw = context.getPrintWriter();
		
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