package sk.seges.sesam.pap.test.selenium.processor;

import java.io.PrintWriter;
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
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;

import org.junit.Ignore;
import org.junit.Test;

import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.configuration.api.OutputDefinition;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.test.selenium.AbstractSeleniumTest;
import sk.seges.sesam.core.test.selenium.configuration.DefaultBromineEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.DefaultCredentialsSettings;
import sk.seges.sesam.core.test.selenium.configuration.DefaultMailSettings;
import sk.seges.sesam.core.test.selenium.configuration.DefaultReportingSettings;
import sk.seges.sesam.core.test.selenium.configuration.DefaultSeleniumEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.DefaultTestEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.annotation.MailConfiguration.Provider;
import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportConfiguration;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumTest;
import sk.seges.sesam.core.test.selenium.configuration.api.Browsers;
import sk.seges.sesam.core.test.selenium.configuration.api.MailSettings;
import sk.seges.sesam.core.test.selenium.configuration.api.TestEnvironment;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class SeleniumTestProcessor extends AbstractConfigurableProcessor {

	static boolean configured = false;
	
	protected ElementKind getElementKind() {
		return ElementKind.CLASS;
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> annotations = new HashSet<String>();
		annotations.add(SeleniumTest.class.getCanonicalName());
		return annotations;
	}
	
	@Override
	protected Type[] getImports() {
		return new Type[] {
			TestEnvironment.class,
			DefaultTestEnvironment.class,
			DefaultSeleniumEnvironment.class,
			DefaultBromineEnvironment.class,
			DefaultCredentialsSettings.class,
			DefaultMailSettings.class,
			MailSettings.class,
			Browsers.class,
			AbstractSeleniumTest.class,
			Provider.class,
			ReportConfiguration.class,
			DefaultReportingSettings.class,
		};
	}
	
	public static final ImmutableType getOutputClass(ImmutableType mutableType) {
		return mutableType.addClassSufix("Configuration");
	}
	
	@Override
	protected Type[] getOutputDefinition(OutputDefinition type, TypeElement typeElement) {
		switch (type) {
		case OUTPUT_SUPERCLASS:
			return new Type[] {nameTypesUtils.toImmutableType(typeElement)};
		}
		
		return super.getOutputDefinition(type, typeElement);
	}
	
	@Override
	protected NamedType[] getTargetClassNames(ImmutableType mutableType) {
		return new NamedType[] {
			getOutputClass(mutableType)
		};
	};

	protected void cloneConstructor(ExecutableElement constructor, NamedType outputClass, PrintWriter pw) {

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
	protected boolean processElement(Element element, RoundEnvironment roundEnv) {
		if (!processingEnv.getTypeUtils().isAssignable(element.asType(), processingEnv.getElementUtils().getTypeElement(AbstractSeleniumTest.class.getCanonicalName()).asType())) {
			return !supportProcessorChain();
		}
//		if (!configured) {
//			SeleniumTestConfigurationProcessor seleniumTestConfigurationProcessor = new SeleniumTestConfigurationProcessor();
//			seleniumTestConfigurationProcessor.init(processingEnv);
//			Set<TypeElement> annotations = new HashSet<TypeElement>();
//			annotations.add(processingEnv.getElementUtils().getTypeElement(SeleniumTestConfiguration.class.getCanonicalName()));
//			seleniumTestConfigurationProcessor.process(annotations, roundEnv);
//			configured = true;
//		}
//		if (roundEnv.processingOver()) {
//			configured = false;
//		}
		return super.processElement(element, roundEnv);
	}

	@Override
	protected void writeClassAnnotations(Element el, NamedType outputName, PrintWriter pw) {
		pw.println("@" + Ignore.class.getCanonicalName());
	}
	
	@Override
	protected void processElement(TypeElement element, NamedType outputClass, RoundEnvironment roundEnv, PrintWriter pw) {
		List<ExecutableElement> constructors = ElementFilter.constructorsIn(element.getEnclosedElements());
		
		for (ExecutableElement constructor: constructors) {
			cloneConstructor(constructor, outputClass, pw);
		}
		
		List<ExecutableElement> methods = ElementFilter.methodsIn(element.getEnclosedElements());

		pw.println("public " + outputClass.getSimpleName() + "() {");
		pw.println("super();");
		pw.println("configure(this);");
		pw.println("}");
		pw.println("");

		new ConfigurationProcessor(processingEnv).createConfiguration(element, pw);

		pw.println("@Override");
		pw.println("public void runTests() {");
		
		for (ExecutableElement method: methods) {
			Test annotation = method.getAnnotation(Test.class);
			if (annotation != null) {
				pw.println(method.getSimpleName().toString() + "();");
			}
		}

		pw.println("}");
	}
}