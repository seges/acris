package sk.seges.sesam.pap.configuration.printer;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.configuration.model.SettingsContext;
import sk.seges.sesam.pap.configuration.printer.api.SettingsElementPrinter;

public class JavaDocPrinter implements SettingsElementPrinter {

	private FormattedPrintWriter pw;
	
	public JavaDocPrinter(FormattedPrintWriter pw, ProcessingEnvironment pe) {
		this.pw = pw;
	}
	
	@Override
	public void initialize(TypeElement type, NamedType outputName) {}

	@Override
	public void print(SettingsContext context) {
		if (context.getParameter() == null) {
			return;
		}
		
		pw.println("/**");
		pw.println("* " + context.getParameter().description());
		pw.println("*/");
	}

	@Override
	public void finish(TypeElement type) {}
}