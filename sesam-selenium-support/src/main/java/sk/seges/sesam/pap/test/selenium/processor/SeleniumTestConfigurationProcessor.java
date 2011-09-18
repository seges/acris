package sk.seges.sesam.pap.test.selenium.processor;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.ElementFilter;

import org.junit.Ignore;

import sk.seges.sesam.core.configuration.annotation.Configuration;
import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.configuration.api.OutputDefinition;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.core.test.selenium.AbstractSeleniumTest;
import sk.seges.sesam.core.test.selenium.configuration.DefaultTestSettings;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumTest;
import sk.seges.sesam.pap.configuration.model.SettingsTypeElement;
import sk.seges.sesam.pap.test.selenium.processor.model.SeleniumSettingsContext;
import sk.seges.sesam.pap.test.selenium.processor.model.SeleniumSettingsProviderTypeElement;
import sk.seges.sesam.pap.test.selenium.processor.model.SeleniumTestTypeElement;
import sk.seges.sesam.pap.test.selenium.processor.printer.SettingsInitializerPrinter;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class SeleniumTestConfigurationProcessor extends AbstractConfigurableProcessor {
	
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
	protected NamedType[] getTargetClassNames(ImmutableType mutableType) {
		return new NamedType[] {
				new SeleniumTestTypeElement((TypeElement)((DeclaredType)mutableType.asType()).asElement(), processingEnv).getConfiguration()
		};
	};

	@Override
	protected Type[] getOutputDefinition(OutputDefinition type, TypeElement typeElement) {
		switch (type) {
		case OUTPUT_SUPERCLASS:
			return new Type[] { DefaultTestSettings.class };
		case OUTPUT_INTERFACES:
			return new Type[] {
					new SeleniumSettingsProviderTypeElement(new SeleniumTestTypeElement(typeElement, processingEnv).getSeleniumSuite(), processingEnv)
			};
		}
		
		return super.getOutputDefinition(type, typeElement);
	}
	
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
		return super.processElement(element, roundEnv);
	}

	@Override
	protected void writeClassAnnotations(Element el, NamedType outputName, PrintWriter pw) {
		pw.println("@" + Ignore.class.getCanonicalName());
	}
	
	@Override
	protected void processElement(TypeElement element, NamedType outputClass, RoundEnvironment roundEnv, FormattedPrintWriter pw) {
		List<ExecutableElement> constructors = ElementFilter.constructorsIn(element.getEnclosedElements());
		
		for (ExecutableElement constructor: constructors) {
			cloneConstructor(constructor, outputClass, pw);
		}

		ArrayList<Element> configurationElements = new ArrayList<Element>(getClassPathTypes().getElementsAnnotatedWith(Configuration.class, roundEnv));
		
		Collections.sort(configurationElements, new Comparator<Element>() {

			@Override
			public int compare(Element o1, Element o2) {
				return o1.toString().compareTo(o2.toString());
			}
		});

		SettingsInitializerPrinter settingsInitializerPrinter = new SettingsInitializerPrinter(pw, processingEnv);
		
		SeleniumTestTypeElement seleniumTestElement = new SeleniumTestTypeElement(element, processingEnv);
				
		for (Element configurationElement: configurationElements) {
			
			SettingsTypeElement settingsTypeElement = new SettingsTypeElement((DeclaredType)configurationElement.asType(), processingEnv);
//			if (settingsTypeElement.exists()) {
				settingsInitializerPrinter.initialize(seleniumTestElement, outputClass);
				SeleniumSettingsContext settingsContext = new SeleniumSettingsContext();
				settingsContext.setSeleniumTest(seleniumTestElement);
				settingsContext.setSettings(settingsTypeElement);
				settingsInitializerPrinter.print(settingsContext);
				settingsInitializerPrinter.finish();
//			}
		}
	}
}