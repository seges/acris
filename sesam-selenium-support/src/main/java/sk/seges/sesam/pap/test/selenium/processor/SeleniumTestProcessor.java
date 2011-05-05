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

import org.junit.Test;

import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.model.api.MutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.test.selenium.annotation.SeleniumTest;
import sk.seges.sesam.core.test.selenium.configuration.api.TestEnvironment;
import sk.seges.sesam.test.selenium.AbstractSeleniumTest;

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
			TestEnvironment.class
		};
	}
	
	public static final MutableType getOutputClass(MutableType mutableType) {
		return mutableType.addClassSufix("Configuration");
	}
	
	@Override
	protected Type[] getConfigurationTypes(DefaultConfigurationType type, TypeElement typeElement) {

		switch (type) {
			case OUTPUT_SUPERCLASS:
				return new Type[] {MutableType.THIS};
		}
		
		return super.getConfigurationTypes(type, typeElement);
	}
	
	@Override
	protected NamedType[] getTargetClassNames(MutableType mutableType) {
		return new NamedType[] {
			getOutputClass(mutableType)
		};
	};

	protected boolean cloneConstructor(ExecutableElement constructor, NamedType outputClass, PrintWriter pw) {

		for (Modifier modifier: constructor.getModifiers()) {
			if (modifier.equals(Modifier.PRIVATE)) {
				return false;
			}
		}
		
		for (Modifier modifier: constructor.getModifiers()) {
			pw.print(modifier.toString() + " ");
		}

		pw.print(outputClass.getSimpleName() + "(");
		
		List<? extends VariableElement> parameters = constructor.getParameters();
		
		boolean result = false;
		
		int i = 0;
		for (VariableElement parameter: parameters) {
			if (i > 0) {
				pw.print(", ");
			}
			if (i == 0 && parameter.asType().toString().equals(TestEnvironment.class.getCanonicalName())) {
				result = true;
			}
			pw.print(parameter.asType().toString() + " " + parameter.getSimpleName().toString());
			i++;
		}

		if (i > 1) {
			result = false;
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
		
		return result;
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
	protected void processElement(TypeElement element, NamedType outputClass, RoundEnvironment roundEnv, PrintWriter pw) {
		List<ExecutableElement> constructors = ElementFilter.constructorsIn(element.getEnclosedElements());
		
		boolean hasEnvironemntConstructor = false;
		
		for (ExecutableElement constructor: constructors) {
			hasEnvironemntConstructor |= cloneConstructor(constructor, outputClass, pw);
		}
		
		List<ExecutableElement> methods = ElementFilter.methodsIn(element.getEnclosedElements());

		if (!hasEnvironemntConstructor) {
			pw.println("public " + outputClass.getSimpleName() + "(" + TestEnvironment.class.getSimpleName() + " testEnvironment) {");
			pw.println("super();");
			pw.println("this.testEnvironment = testEnvironment;");
			pw.println("}");
			pw.println("");
		}
		
		pw.println("@Override");
		pw.println("public void runTests() throws Exception {");
		
		for (ExecutableElement method: methods) {
			Test annotation = method.getAnnotation(Test.class);
			if (annotation != null) {
				pw.println(method.getSimpleName().toString() + "();");
			}
		}

		pw.println("}");
	}
}