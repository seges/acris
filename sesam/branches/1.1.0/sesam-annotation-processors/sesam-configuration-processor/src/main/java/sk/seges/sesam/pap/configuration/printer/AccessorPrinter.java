package sk.seges.sesam.pap.configuration.printer;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.configuration.model.SettingsContext;
import sk.seges.sesam.pap.configuration.printer.api.SettingsElementPrinter;

public class AccessorPrinter extends AbstractSettingsElementPrinter implements SettingsElementPrinter {

	private FormattedPrintWriter pw;
	
	public AccessorPrinter(FormattedPrintWriter pw, ProcessingEnvironment pe) {
		super(pe);
		this.pw = pw;
	}
	
	@Override
	public void initialize(TypeElement type, NamedType outputName) {}

	@Override
	public void print(SettingsContext context) {

		String fieldName = context.getFieldName();

		if (context.getNestedElement() != null) {
			pw.println("public " + context.getNestedOutputName().getSimpleName() +  " " + MethodHelper.toGetter(fieldName) + " {");
		} else {
			TypeMirror unboxedReturnType = unboxType(context.getMethod().getReturnType());
			pw.println("public ", unboxedReturnType, " " + MethodHelper.toGetter(fieldName) + " {");
		}
		pw.println("return " + fieldName + ";");
		pw.println("}");
		pw.println("");

		if (context.getNestedElement() != null) {
			pw.println("public void " + MethodHelper.toSetter(fieldName) + "(" + context.getNestedOutputName().getSimpleName() + " " + fieldName + ") {");
		} else {
			pw.println("public void " + MethodHelper.toSetter(fieldName) + "(", unboxType(context.getMethod().getReturnType()), " " + fieldName + ") {");
		}
		pw.println("this." + fieldName + " = " + fieldName + ";");
		pw.println("}");
		pw.println("");
	}

	@Override
	public void finish(TypeElement type) {}
}