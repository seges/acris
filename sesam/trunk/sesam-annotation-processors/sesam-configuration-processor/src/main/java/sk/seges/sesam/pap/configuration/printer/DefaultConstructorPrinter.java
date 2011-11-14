package sk.seges.sesam.pap.configuration.printer;

import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.configuration.model.setting.SettingsContext;

public class DefaultConstructorPrinter extends AbstractSettingsElementPrinter {

	private FormattedPrintWriter pw;
	
	public DefaultConstructorPrinter(FormattedPrintWriter pw, MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.pw = pw;
	}

	@Override
	public void initialize(TypeElement type, MutableDeclaredType outputName) {
		pw.println("public " + outputName.getSimpleName() + "() {");
	}

	@Override
	public void print(SettingsContext context) {}

	@Override
	public void finish(TypeElement type) {
		pw.println("}");
		pw.println();
	}

}