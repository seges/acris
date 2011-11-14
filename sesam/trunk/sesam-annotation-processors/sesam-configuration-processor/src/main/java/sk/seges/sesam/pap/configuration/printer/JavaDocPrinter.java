package sk.seges.sesam.pap.configuration.printer;

import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.configuration.model.setting.SettingsContext;

public class JavaDocPrinter extends AbstractSettingsElementPrinter {

	private FormattedPrintWriter pw;
	
	public JavaDocPrinter(FormattedPrintWriter pw, MutableProcessingEnvironment pe) {
		super(pe);
		this.pw = pw;
	}
	
	@Override
	public void initialize(TypeElement type, MutableDeclaredType outputName) {}

	@Override
	public void print(SettingsContext context) {
		if (context.getNestedElement() != null) {
			return;
		}
		
		pw.println("/**");
		pw.println("* " + context.getParameterDescription());
		pw.println("*/");
	}

	@Override
	public void finish(TypeElement type) {}
}