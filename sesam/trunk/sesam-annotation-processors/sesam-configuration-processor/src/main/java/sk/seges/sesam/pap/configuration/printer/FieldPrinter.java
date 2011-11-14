package sk.seges.sesam.pap.configuration.printer;

import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.configuration.model.setting.SettingsContext;

public class FieldPrinter extends AbstractSettingsElementPrinter {

	protected FormattedPrintWriter pw;
	
	public FieldPrinter(FormattedPrintWriter pw, MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.pw = pw;
	}
	
	@Override
	public void initialize(TypeElement type, MutableDeclaredType outputName) {}

	@Override
	public void print(SettingsContext context) {
		if (context.getNestedElement() != null) {
			pw.println("private ", context.getNestedMutableType(), " " + context.getFieldName() + ";");
			pw.println();
		} else {
			pw.println("private ", boxType(context.getMethod().getReturnType()), " " + context.getMethod().getSimpleName().toString() + ";");
			pw.println();
		}
	}

	@Override
	public void finish(TypeElement type) {}

}