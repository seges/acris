package sk.seges.sesam.pap.configuration.printer;

import java.io.PrintWriter;

import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.pap.configuration.model.SettingsContext;
import sk.seges.sesam.pap.configuration.printer.api.SettingsElementPrinter;

public class EnumeratedConstructorBodyPrinter implements SettingsElementPrinter {

	private PrintWriter pw;
	
	public EnumeratedConstructorBodyPrinter(PrintWriter pw) {
		this.pw = pw;
	}

	@Override
	public void initialize(TypeElement type, MutableDeclaredType outputName) {
	}

	@Override
	public void print(SettingsContext context) {
		pw.println("this." + context.getFieldName() + " = " + context.getFieldName() + ";");
	}

	@Override
	public void finish(TypeElement type) {
		pw.println("}");
		pw.println();
	}
}