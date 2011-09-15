package sk.seges.sesam.pap.configuration.printer;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.configuration.model.SettingsContext;
import sk.seges.sesam.pap.configuration.model.SettingsTypeElement;
import sk.seges.sesam.pap.configuration.printer.api.SettingsElementPrinter;

public class FieldPrinter extends AbstractSettingsElementPrinter implements SettingsElementPrinter {

	private FormattedPrintWriter pw;
	
	public FieldPrinter(FormattedPrintWriter pw, ProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.pw = pw;
	}
	
	@Override
	public void initialize(TypeElement type, NamedType outputName) {}

	@Override
	public void print(SettingsContext context) {
		if (context.getParameter() == null) {
			NamedType nestedOutputName = new SettingsTypeElement((DeclaredType) context.getNestedElement().asType(), processingEnv);
			pw.println("private " + nestedOutputName.getSimpleName() + " " + context.getFieldName() + ";");
			pw.println();
		} else {
			pw.println("private ", unboxType(context.getMethod().getReturnType()), " " + context.getMethod().getSimpleName().toString() + ";");
			pw.println();
		}
	}

	@Override
	public void finish(TypeElement type) {}

}