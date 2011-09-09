package sk.seges.sesam.pap.configuration.printer;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.configuration.model.ProcessorContext;
import sk.seges.sesam.pap.configuration.printer.api.ElementPrinter;

public class JavaDocPrinter implements ElementPrinter {

	private FormattedPrintWriter pw;
	
	public JavaDocPrinter(FormattedPrintWriter pw, ProcessingEnvironment pe) {
		this.pw = pw;
	}
	
	@Override
	public void initialize(TypeElement type, NamedType outputName) {}

	@Override
	public void print(ProcessorContext context) {
		pw.println("/**");
		pw.println("* " + context.getParameter().description());
		pw.println("*/");
	}

	@Override
	public void finish(TypeElement type) {}
}