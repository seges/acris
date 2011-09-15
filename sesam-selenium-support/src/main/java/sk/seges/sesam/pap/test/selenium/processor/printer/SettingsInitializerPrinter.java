package sk.seges.sesam.pap.test.selenium.processor.printer;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;

import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.configuration.model.SettingsTypeElement;
import sk.seges.sesam.pap.test.selenium.processor.model.SeleniumSettingsContext;
import sk.seges.sesam.pap.test.selenium.processor.model.SeleniumTestTypeElement;
import sk.seges.sesam.pap.test.selenium.processor.printer.api.SeleniumSettingsElementPrinter;

public class SettingsInitializerPrinter implements SeleniumSettingsElementPrinter {

	private static final String RESULT_NAME = "result";
	
	private FormattedPrintWriter pw;
	private ProcessingEnvironment processingEnv;
	
	public SettingsInitializerPrinter(FormattedPrintWriter pw, ProcessingEnvironment processingEnv) {
		this.pw = pw;
		this.processingEnv = processingEnv;
	}
	
	@Override
	public void initialize(SeleniumTestTypeElement seleniumTestElement, NamedType outputName) {
	}

	@Override
	public void print(SeleniumSettingsContext settingsContext) {


		SettingsTypeElement settingsTypeElement = settingsContext.getSettings();

		pw.println("public ", settingsTypeElement, " get" + settingsTypeElement.getSimpleName() + "() {");
		pw.println(settingsTypeElement, " " + RESULT_NAME + " = new ", settingsTypeElement, "(collectSystemProperties());");

		AnnotationMirror annotationMirror = settingsTypeElement.getAnnotationMirrorForElement(settingsContext.getSeleniumTest().asElement());
		
		if (annotationMirror != null) {
			//Settings are defined in the test case, so merge them firstly
			pw.print(RESULT_NAME + ".merge(");
			SettingInstancerPrinter settingInstancerPrinter = new SettingInstancerPrinter(annotationMirror, processingEnv, pw, false);
			settingInstancerPrinter.print(settingsTypeElement, settingsTypeElement);
			pw.println(");");
		}

		annotationMirror = settingsTypeElement.getAnnotationMirrorForElement(settingsContext.getSeleniumTest().getSeleniumSuite().asElement());

		if (annotationMirror != null) {
			//merging settings from the test suite
			pw.print(RESULT_NAME + ".merge(");
			SettingInstancerPrinter settingInstancerPrinter = new SettingInstancerPrinter(annotationMirror, processingEnv, pw, true);
			settingInstancerPrinter.print(settingsTypeElement, settingsTypeElement);
			pw.println(");");
		}
	}
	
	@Override
	public void finish() {
		pw.println("return " + RESULT_NAME + ";");	
		pw.println("}");
		pw.println("");
	}
}