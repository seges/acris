package sk.seges.sesam.pap.configuration.printer;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

import sk.seges.sesam.core.pap.builder.NameTypeUtils;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.configuration.model.SettingsContext;
import sk.seges.sesam.pap.configuration.model.SettingsTypeElement;
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
		if (context.getNestedElement() == null) {
			return;
		}
		NamedType nestedOutputName = new SettingsTypeElement((DeclaredType) context.getNestedElement().asType(), processingEnv);
		pw.println("public class " + nestedOutputName.getSimpleName() + " {");
		settingsProcessor.processAnnotation(context.getNestedElement(), nestedOutputName, pw);
		pw.println("}");

	}

	@Override
	public void finish(TypeElement type) {}
}
