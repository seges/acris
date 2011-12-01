package sk.seges.sesam.pap.configuration.printer;

import java.io.PrintWriter;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.pap.configuration.model.setting.SettingsContext;

public class EnumeratedConstructorBodyPrinter extends AbstractSettingsElementPrinter {

	private PrintWriter pw;
	
	public EnumeratedConstructorBodyPrinter(PrintWriter pw, MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
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

	@Override
	public ElementKind getSupportedType() {
		return ElementKind.METHOD;
	}
}