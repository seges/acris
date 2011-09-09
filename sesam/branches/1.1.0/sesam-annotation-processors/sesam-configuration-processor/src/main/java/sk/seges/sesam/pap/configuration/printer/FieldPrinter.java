package sk.seges.sesam.pap.configuration.printer;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.configuration.model.ProcessorContext;
import sk.seges.sesam.pap.configuration.printer.api.ElementPrinter;


public class FieldPrinter extends AbstractElementPrinter implements ElementPrinter {

	private FormattedPrintWriter pw;
	
	public FieldPrinter(FormattedPrintWriter pw, ProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.pw = pw;
	}
	
	@Override
	public void initialize(TypeElement type, NamedType outputName) {}

	@Override
	public void print(ProcessorContext context) {
		pw.println("private ", unboxType(context.getMethod().getReturnType()), " " + context.getMethod().getSimpleName().toString() + ";");
		pw.println();
	}

	@Override
	public void finish(TypeElement type) {}

}
