package sk.seges.sesam.pap.test.selenium.processor;

import java.util.List;
import java.util.Set;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.processor.MutableAnnotationProcessor;
import sk.seges.sesam.core.pap.utils.ElementSorter;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumTest;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumTestCase;
import sk.seges.sesam.pap.test.selenium.processor.accessor.SeleniumTestCaseAccessor;
import sk.seges.sesam.pap.test.selenium.processor.configurer.SeleniumSuiteProcessorConfigurer;
import sk.seges.sesam.pap.test.selenium.processor.model.SeleniumSuiteRunnerType;
import sk.seges.sesam.pap.test.selenium.processor.model.SeleniumSuiteType;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class SeleniumSuiteRunnerProcessor extends MutableAnnotationProcessor {

	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] {
			new SeleniumSuiteType(context.getTypeElement(), processingEnv).getSuiteRunner()
		};
	}

	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new SeleniumSuiteProcessorConfigurer();
	}
		
	@Override
	protected void processElement(ProcessorContext context) {
		
		FormattedPrintWriter pw = context.getPrintWriter();

		pw.println("public static void main(String[] args) {");
		pw.println("new ",context.getOutputType(), "().run();");
		pw.println("}");
		pw.println();
		pw.println("public void run() {");
		
		Set<? extends Element> seleniumTestClasses = getClassPathTypes().getElementsAnnotatedWith(SeleniumTestCase.class);

		pw.println();
		
		for (Element seleniumTestClass: seleniumTestClasses) {

			if (new SeleniumTestCaseAccessor(seleniumTestClass, processingEnv).isAssignedToRunner((SeleniumSuiteRunnerType)context.getOutputType())) {
			
				List<ExecutableElement> methods = ElementFilter.methodsIn(seleniumTestClass.getEnclosedElements());
	
				ElementSorter.sort(methods);
				
				for (ExecutableElement method: methods) {
					SeleniumTest annotation = method.getAnnotation(SeleniumTest.class);
					if (annotation != null) {
						pw.println("try {");
						
						String testName = MethodHelper.toField(seleniumTestClass.getSimpleName().toString());
						
						pw.println(seleniumTestClass, " " + testName + " = new ", seleniumTestClass, "();");
						pw.println("getPrinter(" + testName + ").initialize(getTestResult(" + testName + "));");
						//TODO find before annotation
						pw.println(testName, ".setUp();");
						pw.println("try {");
						pw.println(testName, "." + method.getSimpleName().toString() + "();");
						pw.println("} catch (", Exception.class, " ex) {");
						pw.println(System.class,".out.println(ex);");
						pw.println("} finally {");
						//TODO find after annotation
						pw.println(testName, ".tearDown();");
						pw.println("getTestResult(" + testName + ").addTestCaseResult(" + testName + ".getTestInfo());");
						pw.println("}");
						pw.println("} catch (", Exception.class, " ex) {");
						pw.println(System.class,".out.println(ex);");
						pw.println("}");
					}
				}
			}
		}

		pw.println("printReports();");
		
		pw.println("}");
		pw.println("");
	}
}