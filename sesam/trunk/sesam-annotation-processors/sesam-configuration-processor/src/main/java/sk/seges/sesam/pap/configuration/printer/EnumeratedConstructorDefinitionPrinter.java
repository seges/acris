package sk.seges.sesam.pap.configuration.printer;

import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.configuration.model.setting.SettingsContext;

public class EnumeratedConstructorDefinitionPrinter extends AbstractSettingsElementPrinter {

	private FormattedPrintWriter pw;
	private int index = 0;
	
	public EnumeratedConstructorDefinitionPrinter(FormattedPrintWriter pw, MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.pw = pw;
	}

	@Override
	public void initialize(TypeElement type, MutableDeclaredType outputName) {
		pw.print("public " + outputName.getSimpleName() + "(");
	}

	@Override
	public void print(SettingsContext context) {
		if (index > 0) {
			pw.print(", ");
		}

		if (context.getNestedElement() != null) {
			pw.print(context.getNestedMutableType().getSimpleName() + " " + context.getMethod().getSimpleName().toString());
		} else {
			pw.print(boxType(context.getMethod().getReturnType()), " " + context.getMethod().getSimpleName().toString());
		}
		
		index++;
	}

	@Override
	public void finish(TypeElement type) {
		pw.println(") {");
	}
}