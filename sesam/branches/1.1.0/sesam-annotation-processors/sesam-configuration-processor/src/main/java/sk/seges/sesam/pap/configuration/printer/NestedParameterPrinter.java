package sk.seges.sesam.pap.configuration.printer;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.configuration.annotation.Settings;
import sk.seges.sesam.core.pap.builder.NameTypeUtils;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.configuration.model.SettingsContext;
import sk.seges.sesam.pap.configuration.printer.api.SettingsElementPrinter;
import sk.seges.sesam.pap.configuration.processor.SettingsProcessor;

public class NestedParameterPrinter extends AbstractSettingsElementPrinter implements SettingsElementPrinter {

	private FormattedPrintWriter pw;
	private SettingsProcessor settingsProcessor;
	
	public NestedParameterPrinter(FormattedPrintWriter pw, SettingsProcessor configurationProcessor, ProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.nameTypesUtils = new NameTypeUtils(processingEnv);
		this.pw = pw;
		this.settingsProcessor = configurationProcessor;
	}

	@Override
	public void initialize(TypeElement type, NamedType outputName) {}

	@Override
	public void print(SettingsContext context) {
		if (context.getNestedElement() == null || context.isNestedElementExists()) {
			return;
		}
		pw.println("@", Settings.class, "(configuration = ", context.getNestedElement(), ".class)");
		pw.println("public static class " + context.getNestedOutputName().getSimpleName() + " {");
		settingsProcessor.processAnnotation(context.getNestedElement(), context.getNestedOutputName(), pw);
		pw.println("}");

	}

	@Override
	public void finish(TypeElement type) {}
}
