package sk.seges.sesam.pap.test.selenium.processor.printer;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.configuration.model.setting.SettingsTypeElement;
import sk.seges.sesam.pap.test.selenium.processor.model.SeleniumSettingsContext;
import sk.seges.sesam.pap.test.selenium.processor.model.SeleniumTestCaseType;
import sk.seges.sesam.pap.test.selenium.processor.printer.api.SeleniumSettingsElementPrinter;

public class SettingsInitializerPrinter implements SeleniumSettingsElementPrinter {

	private static final String RESULT_NAME = "result";
	
	private FormattedPrintWriter pw;
	private MutableProcessingEnvironment processingEnv;
	
	public SettingsInitializerPrinter(FormattedPrintWriter pw, MutableProcessingEnvironment processingEnv) {
		this.pw = pw;
		this.processingEnv = processingEnv;
	}
	
	@Override
	public void initialize(SeleniumTestCaseType seleniumTestElement, MutableDeclaredType outputName) {
	}

	@Override
	public void print(SeleniumSettingsContext settingsContext) {

		SettingsTypeElement settingsTypeElement = settingsContext.getSettings();

		pw.println("public ", settingsTypeElement, " get" + settingsTypeElement.getSimpleName() + "() {");
		pw.println(settingsTypeElement, " " + RESULT_NAME + " = new ", settingsTypeElement, "(collectSystemProperties());");

		AnnotationMirror annotationMirror = settingsTypeElement.getAnnotationMirrorForElement(settingsContext.getSeleniumTestCase().asElement());
		
		if (annotationMirror != null) {
			//Settings are defined in the test case, so merge them firstly
			pw.print(RESULT_NAME + ".merge(");
			SettingInstancerPrinter settingInstancerPrinter = new SettingInstancerPrinter(annotationMirror, processingEnv, pw, false);
			settingInstancerPrinter.print(settingsTypeElement, settingsTypeElement);
			pw.println(");");
		}

		TypeElement configuration = settingsContext.getSeleniumTestCase().getConfiguration();
		annotationMirror = settingsTypeElement.getAnnotationMirrorForElement(configuration);

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