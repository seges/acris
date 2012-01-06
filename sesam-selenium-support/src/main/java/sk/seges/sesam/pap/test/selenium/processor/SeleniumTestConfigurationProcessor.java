package sk.seges.sesam.pap.test.selenium.processor;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import org.junit.Ignore;

import sk.seges.sesam.core.configuration.annotation.Configuration;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.processor.MutableAnnotationProcessor;
import sk.seges.sesam.core.pap.utils.ElementSorter;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.core.test.selenium.AbstractSeleniumTest;
import sk.seges.sesam.pap.configuration.model.setting.SettingsTypeElement;
import sk.seges.sesam.pap.test.selenium.processor.configurer.SeleniumTestProcessorConfigurer;
import sk.seges.sesam.pap.test.selenium.processor.model.SeleniumSettingsContext;
import sk.seges.sesam.pap.test.selenium.processor.model.SeleniumTestCaseType;
import sk.seges.sesam.pap.test.selenium.processor.printer.SettingsInitializerPrinter;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class SeleniumTestConfigurationProcessor extends MutableAnnotationProcessor {
	
	protected ElementKind getElementKind() {
		return ElementKind.CLASS;
	}

	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new SeleniumTestProcessorConfigurer();
	}

	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] { 
				new SeleniumTestCaseType(context.getTypeElement(), processingEnv).getSettings()
		};
	}
	
	protected void cloneConstructor(ExecutableElement constructor, MutableDeclaredType outputClass, PrintWriter pw) {

		for (Modifier modifier: constructor.getModifiers()) {
			if (modifier.equals(Modifier.PRIVATE)) {
				return;
			}
		}
		
		if (constructor.getParameters() == null || constructor.getParameters().size() == 0) {
			return;
		}
		
		for (Modifier modifier: constructor.getModifiers()) {
			pw.print(modifier.toString() + " ");
		}

		pw.print(outputClass.getSimpleName() + "(");
		
		List<? extends VariableElement> parameters = constructor.getParameters();
				
		int i = 0;
		for (VariableElement parameter: parameters) {
			if (i > 0) {
				pw.print(", ");
			}
			pw.print(parameter.asType().toString() + " " + parameter.getSimpleName().toString());
			i++;
		}

		pw.println(") {");
		pw.print("super(");
		i = 0;
		for (VariableElement parameter: parameters) {
			if (i > 0) {
				pw.print(", ");
			}
			pw.print(parameter.getSimpleName().toString());
			i++;
		}
		pw.println(");");
		pw.println("}");
		pw.println("");
	}

	@Override
	protected boolean supportProcessorChain() {
		return false;
	}
	
	@Override
	protected boolean checkPreconditions(ProcessorContext context, boolean alreadyExists) {
		return processingEnv.getTypeUtils().isAssignable(context.getTypeElement().asType(), 
				processingEnv.getElementUtils().getTypeElement(AbstractSeleniumTest.class.getCanonicalName()).asType());
	}
	
	@Override
	protected void printAnnotations(ProcessorContext context) {
		context.getPrintWriter().println("@" + Ignore.class.getCanonicalName());
	}

	@Override
	protected void processElement(ProcessorContext context) {
		
		FormattedPrintWriter pw = context.getPrintWriter();
		
		List<ExecutableElement> constructors = ElementFilter.constructorsIn(context.getTypeElement().getEnclosedElements());
		
		for (ExecutableElement constructor: constructors) {
			cloneConstructor(constructor, context.getOutputType(), context.getPrintWriter());
		}

		ArrayList<Element> configurationElements = new ArrayList<Element>(getClassPathTypes().getElementsAnnotatedWith(Configuration.class, roundEnv));
		
		ElementSorter.sort(configurationElements);

		SettingsInitializerPrinter settingsInitializerPrinter = new SettingsInitializerPrinter(pw, processingEnv);
		
		SeleniumTestCaseType seleniumTestElement = new SeleniumTestCaseType(context.getTypeElement(), processingEnv);

		processingEnv.getMessager().printMessage(Kind.NOTE, configurationElements.size() + " configurations found");
		
		for (Element configurationElement: configurationElements) {
			
			SettingsTypeElement settingsTypeElement = new SettingsTypeElement((DeclaredType)configurationElement.asType(), processingEnv);
			//We have to handle all available settings because they can be defined in the command line, not using the annotations
//			if (settingsTypeElement.exists()) {
				settingsInitializerPrinter.initialize(seleniumTestElement, context.getOutputType());
				SeleniumSettingsContext settingsContext = new SeleniumSettingsContext();
				settingsContext.setSeleniumTestCase(seleniumTestElement);
				settingsContext.setSettings(settingsTypeElement);
				settingsInitializerPrinter.print(settingsContext);
				settingsInitializerPrinter.finish();
//			}
		}
	}
}