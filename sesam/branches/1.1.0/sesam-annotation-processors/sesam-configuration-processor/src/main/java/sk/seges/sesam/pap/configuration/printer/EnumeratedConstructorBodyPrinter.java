package sk.seges.sesam.pap.configuration.printer;

import java.io.PrintWriter;

import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.pap.configuration.model.ProcessorContext;
import sk.seges.sesam.pap.configuration.printer.api.ElementPrinter;

public class EnumeratedConstructorBodyPrinter implements ElementPrinter {

	private PrintWriter pw;
	
	public EnumeratedConstructorBodyPrinter(PrintWriter pw) {
		this.pw = pw;
	}

	@Override
	public void initialize(TypeElement type, NamedType outputName) {
	}

	@Override
	public void print(ProcessorContext context) {
		String name = context.getMethod().getSimpleName().toString();
		pw.println("this." + name + " = " + name + ";");
	}

	@Override
	public void finish(TypeElement type) {
		pw.println("}");
		pw.println();
	}
}