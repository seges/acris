package sk.seges.sesam.pap.configuration.printer;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.configuration.model.ProcessorContext;
import sk.seges.sesam.pap.configuration.printer.api.ElementPrinter;

public class EnumeratedConstructorDefinitionPrinter extends AbstractElementPrinter implements ElementPrinter {

	private FormattedPrintWriter pw;
	private int index = 0;
	
	public EnumeratedConstructorDefinitionPrinter(FormattedPrintWriter pw, ProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.pw = pw;
	}

	@Override
	public void initialize(TypeElement type, NamedType outputName) {
		pw.print("public " + outputName.getSimpleName() + "(");
	}

	@Override
	public void print(ProcessorContext context) {
		if (index > 0) {
			pw.print(", ");
		}
		pw.print(unboxType(context.getMethod().getReturnType()), " " + context.getMethod().getSimpleName().toString());
		index++;
	}

	@Override
	public void finish(TypeElement type) {
		pw.println(") {");
	}

}
