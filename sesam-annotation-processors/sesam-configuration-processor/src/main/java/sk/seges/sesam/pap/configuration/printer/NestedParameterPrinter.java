package sk.seges.sesam.pap.configuration.printer;

import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.configuration.annotation.Settings;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.configuration.model.setting.SettingsContext;
import sk.seges.sesam.pap.configuration.processor.api.NestableProcessor;

public class NestedParameterPrinter extends AbstractSettingsElementPrinter {

	private FormattedPrintWriter pw;
	private NestableProcessor settingsProcessor;
	
	public NestedParameterPrinter(FormattedPrintWriter pw, NestableProcessor settingsProcessor, MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.pw = pw;
		this.settingsProcessor = settingsProcessor;
	}

	@Override
	public void initialize(TypeElement type, MutableDeclaredType outputName) {}

	@Override
	public void print(SettingsContext context) {
		if (context.getNestedElement() == null || context.isNestedElementExists()) {
			return;
		}
		pw.println("@", Settings.class, "(configuration = ", context.getNestedElement(), ".class)");
		pw.println("public static class " + context.getNestedMutableType().getSimpleName() + " {");
		settingsProcessor.processAnnotation(context.getNestedElement(), context.getNestedMutableType(), pw);
		pw.println("}");

	}

	@Override
	public void finish(TypeElement type) {}
}
