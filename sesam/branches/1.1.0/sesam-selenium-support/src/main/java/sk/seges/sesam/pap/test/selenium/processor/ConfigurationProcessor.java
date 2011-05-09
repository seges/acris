package sk.seges.sesam.pap.test.selenium.processor;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleTypeVisitor6;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.test.selenium.annotation.MailConfiguration;
import sk.seges.sesam.core.test.selenium.annotation.MailConfiguration.Provider;
import sk.seges.sesam.core.test.selenium.annotation.ReportConfiguration;
import sk.seges.sesam.core.test.selenium.annotation.SeleniumTest;
import sk.seges.sesam.core.test.selenium.annotation.SeleniumTestConfiguration;
import sk.seges.sesam.core.test.selenium.configuration.DefaultBromineEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.DefaultMailSettings;
import sk.seges.sesam.core.test.selenium.configuration.DefaultReportingSettings;
import sk.seges.sesam.core.test.selenium.configuration.DefaultSeleniumEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.DefaultTestEnvironment;
import sk.seges.sesam.core.test.selenium.configuration.api.Browsers;
import sk.seges.sesam.test.selenium.AbstractSeleniumTest;

public class ConfigurationProcessor {

	abstract class ConfigurationMerger {

		abstract Class<?> getReturnType();
		
		abstract Class<?> getAnnotation();
		
		abstract String getMethodName();

		abstract void createConfiguration(AnnotationMirror seleniumConfiguration, String suffix, PrintWriter pw, boolean includeDefaults);

		public void createConfiguration(TypeElement element, PrintWriter pw) {
		
			SeleniumTest seleniumTest = element.getAnnotation(SeleniumTest.class);
			DeclaredType suiteRunner = getSuiteRunner(seleniumTest);

			pw.println("private static " + getReturnType().getSimpleName() + " " + getMethodName() + "(" + element.getQualifiedName() + " testElement) {");

			pw.println(getReturnType().getSimpleName() + " result = new " + 
					getReturnType().getSimpleName() + "(testElement.collectSystemProperties());");
			pw.println();
			
			AnnotationMirror seleniumConfigurationAnnotation = getAnnotationMirror(element.asType(), getAnnotation());

			if (seleniumConfigurationAnnotation != null) {
				createConfiguration(seleniumConfigurationAnnotation, "Specific", pw, false);
			}

			if (suiteRunner != null) {
				
				seleniumConfigurationAnnotation = getAnnotationMirror(suiteRunner, getAnnotation());
				
				if (seleniumConfigurationAnnotation != null) {
					createConfiguration(seleniumConfigurationAnnotation, "Root", pw, true);
				} else {
					processingEnv.getMessager().printMessage(Kind.WARNING, "Invalid site runner (" + suiteRunner.toString() + ") specified. Please, specify correct site runner class in the " + SeleniumTest.class.getSimpleName() + " annotation.");
				}			
			} else {
				processingEnv.getMessager().printMessage(Kind.WARNING, "No site runner specified. Please, specify site runner class in the " + SeleniumTest.class.getSimpleName() + " annotation.");
			}

			pw.println("return result;");

			pw.println("}");
			pw.println("");
		}
	}
	
    protected ProcessingEnvironment processingEnv;

    public ConfigurationProcessor(ProcessingEnvironment processingEnv) {
    	this.processingEnv = processingEnv;
    }
    
	public void createConfiguration(TypeElement element, PrintWriter pw) {
		createTestConfiguration(element, pw);
		createMailConfiguration(element, pw);
		createReportConfiguration(element, pw);
		
		pw.println("public static " + AbstractSeleniumTest.class.getSimpleName() + " configure(" + element.getQualifiedName() + " testElement) {");

		pw.println("testElement.setTestEnvironment(getTestConfiguration(testElement));");
		pw.println("testElement.setMailEnvironment(getMailConfiguration(testElement));");
		pw.println("testElement.setReportingSettings(getReportConfiguration(testElement));");

		pw.println("return testElement;");
		pw.println("}");
		pw.println("");
	}

	protected void createMailConfiguration(TypeElement element, PrintWriter pw) {
		
		new ConfigurationMerger() {

			@Override
			Class<?> getReturnType() {
				return DefaultMailSettings.class;
			}

			@Override
			Class<?> getAnnotation() {
				return MailConfiguration.class;
			}

			@Override
			String getMethodName() {
				return "getMailConfiguration";
			}

			@Override
			void createConfiguration(AnnotationMirror seleniumConfiguration, String suffix, PrintWriter pw,
					boolean includeDefaults) {
				createMailConfiguration(seleniumConfiguration, suffix, pw, includeDefaults);
			}
			
		}.createConfiguration(element, pw);		
	}
	
	public DeclaredType getSuiteRunner(SeleniumTest annotation) {
		try {
			return (DeclaredType)processingEnv.getElementUtils().getTypeElement(annotation.suiteRunner().getCanonicalName()).asType();
		} catch (MirroredTypeException mte) {
			return mte.getTypeMirror().accept(new SimpleTypeVisitor6<DeclaredType, Void>() {
				@Override
				public DeclaredType visitDeclared(DeclaredType t, Void p) {
					return t;
				}
			}, null);
		}
	}

	protected String serialize(boolean b) {
		return b ? "true" : "false";
	}
	
	protected String serialize(String s) {
		if (s == null) {
			return null;
		}
		return "\"" + s + "\"";
	}

	protected Integer serialize(Integer s) {
		if (s == null) {
			return 0;
		}
		
		return s;
	}


	protected void createMailConfiguration(AnnotationMirror seleniumMailConfiguration, String suffix, PrintWriter pw, boolean includeDefaults) {
		VariableElement provider = ((VariableElement)getConfigurationValue(seleniumMailConfiguration, "provider", includeDefaults));
		
		String providerName = provider == null ? null : (Provider.class.getSimpleName() + "." + provider.getSimpleName().toString());
	
		pw.println(DefaultMailSettings.class.getSimpleName() + " defaultMailSettings" + suffix + " = new "
				+ DefaultMailSettings.class.getSimpleName() + "("
				+ serialize(NullCheck.checkNull((String)getConfigurationValue(seleniumMailConfiguration, "host", includeDefaults))) + ","
				+ serialize(NullCheck.checkNull((String)getConfigurationValue(seleniumMailConfiguration, "password", includeDefaults))) + ","
				+ serialize(NullCheck.checkNull((String)getConfigurationValue(seleniumMailConfiguration, "mail", includeDefaults))) + ","
				+ providerName + ");");
		
		pw.println("result.merge(defaultMailSettings" + suffix + ");");
	}
	
	protected void createTestConfiguration(AnnotationMirror seleniumTestConfiguration, String suffix, PrintWriter pw, boolean includeDefaults) {

		pw.println(DefaultSeleniumEnvironment.class.getSimpleName() + " defaultSeleniumEnvironment" + suffix + " = new "
				+ DefaultSeleniumEnvironment.class.getSimpleName() + "("
				+ serialize(NullCheck.checkNull((String)getConfigurationValue(seleniumTestConfiguration, "seleniumServer", includeDefaults))) + ","
				+ serialize((Integer)getConfigurationValue(seleniumTestConfiguration, "seleniumPort", includeDefaults)) + ");");
		pw.println(DefaultBromineEnvironment.class.getSimpleName() + " defaultBromineEnvironment" + suffix + " = new "
				+ DefaultBromineEnvironment.class.getSimpleName() + "("
				+ serialize(NullCheck.checkNull((String)getConfigurationValue(seleniumTestConfiguration, "bromineServer", includeDefaults))) + ","
				+ serialize((Integer)getConfigurationValue(seleniumTestConfiguration, "brominePort", includeDefaults)) + ");");
		
		VariableElement browser = ((VariableElement)getConfigurationValue(seleniumTestConfiguration, "browser", includeDefaults));
		
		String browserName = browser == null ? null : (Browsers.class.getSimpleName() + "." + browser.getSimpleName().toString());
		
		pw.println(DefaultTestEnvironment.class.getSimpleName() + " defaultTestEnvironment" + suffix + " = new " + DefaultTestEnvironment.class.getSimpleName()
				+ "(defaultSeleniumEnvironment" + suffix + ", defaultBromineEnvironment" + suffix + ", "
				+ serialize(NullCheck.checkNull((String)getConfigurationValue(seleniumTestConfiguration, "testURL", includeDefaults))) + ", "
				+ serialize(NullCheck.checkNull((String)getConfigurationValue(seleniumTestConfiguration, "testURI", includeDefaults))) + ", "
				+ browserName + ");");
		pw.println("result.merge(defaultTestEnvironment" + suffix + ");");
		pw.println();
	}

	@SuppressWarnings("unchecked")
	protected <T> T getConfigurationValue(AnnotationMirror seleniumConfigurationAnnotation, String method, boolean includeDefaults) {
		
		if (seleniumConfigurationAnnotation == null) {
			return null;
		}

		Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = null;
		
		if (includeDefaults) {
			elementValues = processingEnv.getElementUtils().getElementValuesWithDefaults(seleniumConfigurationAnnotation);
		} else {
			elementValues = seleniumConfigurationAnnotation.getElementValues();
		}
		
		for (Entry<? extends ExecutableElement, ? extends AnnotationValue> entry: elementValues.entrySet()) {
			if (entry.getKey().getSimpleName().toString().equals(method)) {
				return (T)entry.getValue().getValue();
			}
		}
		
		return null;
	}

	protected AnnotationMirror getAnnotationMirror(TypeMirror owner, Class<?> annotationClass) {
		List<? extends AnnotationMirror> annotationMirrors2 = ((DeclaredType)owner).asElement().getAnnotationMirrors();
		processingEnv.getMessager().printMessage(Kind.WARNING, "There are " + annotationMirrors2.size() + " annotations on the " + owner.toString());
		
		List<? extends AnnotationMirror> allAnnotationMirrors = processingEnv.getElementUtils().getAllAnnotationMirrors(processingEnv.getElementUtils().getTypeElement(owner.toString()));
		
		List<? extends AnnotationMirror> annotationMirrors = processingEnv.getElementUtils().getTypeElement(owner.toString()).getAnnotationMirrors();

		SeleniumTestConfiguration st = processingEnv.getElementUtils().getTypeElement(owner.toString()).getAnnotation(SeleniumTestConfiguration.class);
		processingEnv.getMessager().printMessage(Kind.WARNING, "There is " + (st == null ? "not" : "") + " ST on the " + owner.toString());
		
		processingEnv.getMessager().printMessage(Kind.WARNING, "There are " + allAnnotationMirrors.size() + " annotations on the " + owner.toString());
		processingEnv.getMessager().printMessage(Kind.WARNING, "There are " + annotationMirrors.size() + " annotations on the " + owner.toString());

		for (AnnotationMirror annotation: annotationMirrors) {
			Element annotationElement = annotation.getAnnotationType().asElement();
			
			if (((TypeElement)annotationElement).getQualifiedName().toString().equals(annotationClass.getCanonicalName())) {
				return annotation;
			}
			processingEnv.getMessager().printMessage(Kind.NOTE, "Resolving " + annotationClass.getCanonicalName() + " - processing: " + annotation.toString());
		}

		processingEnv.getMessager().printMessage(Kind.WARNING, "No annotation was found for " + annotationClass.getCanonicalName());

		return null;
	}
	
	protected void createTestConfiguration(TypeElement element, PrintWriter pw) {
		new ConfigurationMerger() {

			@Override
			Class<?> getReturnType() {
				return DefaultTestEnvironment.class;
			}

			@Override
			Class<?> getAnnotation() {
				return SeleniumTestConfiguration.class;
			}

			@Override
			String getMethodName() {
				return "getTestConfiguration";
			}

			@Override
			void createConfiguration(AnnotationMirror seleniumConfiguration, String suffix, PrintWriter pw,
					boolean includeDefaults) {
				createTestConfiguration(seleniumConfiguration, suffix, pw, includeDefaults);
			}
			
		}.createConfiguration(element, pw);
	}
	
	protected void createReportConfiguration(TypeElement element, PrintWriter pw) {
		new ConfigurationMerger() {

			@Override
			Class<?> getReturnType() {
				return DefaultReportingSettings.class;
			}

			@Override
			Class<?> getAnnotation() {
				return ReportConfiguration.class;
			}

			@Override
			String getMethodName() {
				return "getReportConfiguration";
			}

			@Override
			void createConfiguration(AnnotationMirror seleniumConfiguration, String suffix, PrintWriter pw,
					boolean includeDefaults) {
				createReportConfiguration(seleniumConfiguration, suffix, pw, includeDefaults);
			}
			
		}.createConfiguration(element, pw);
	}

	protected void createReportConfiguration(AnnotationMirror seleniumReportingConfiguration, String suffix, PrintWriter pw, boolean includeDefaults) {
		
		AnnotationMirror screenshotConfiguration = ((AnnotationMirror)getConfigurationValue(seleniumReportingConfiguration, "screenshotConfiguration", includeDefaults));
		
		pw.println(DefaultReportingSettings.class.getSimpleName() + " defaultReportingSettings" + suffix + " = new "
				+ DefaultReportingSettings.class.getSimpleName() + "("
				+ serialize((Boolean)getConfigurationValue(screenshotConfiguration, "produceScreenshots", includeDefaults)) + ","
				+ serialize(NullCheck.checkNull((String)getConfigurationValue(screenshotConfiguration, "resultDirectory", includeDefaults))) + ","
				+ serialize(NullCheck.checkNull((String)getConfigurationValue(screenshotConfiguration, "screenshotsDirectory", includeDefaults))) + ");");
		
		pw.println("result.merge(defaultReportingSettings" + suffix + ");");
	}

}