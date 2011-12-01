package sk.seges.sesam.pap.configuration.printer;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.configuration.model.setting.SettingsContext;

public class MergePrinter extends AbstractSettingsElementPrinter {

	private String instanceName;
	private FormattedPrintWriter pw;

	public MergePrinter(FormattedPrintWriter pw, MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.pw = pw;
	}
	
	@Override
	public void initialize(TypeElement type, MutableDeclaredType outputName) {
		instanceName = MethodHelper.toField(outputName.getSimpleName());
		pw.println("public " + outputName.getSimpleName() + " merge(" + outputName.getSimpleName() + " " + instanceName + ") {");
		pw.println("if (" + instanceName + " == null) {");
		pw.println("return this;");
		pw.println("}");
	}

	@Override
	public void print(SettingsContext context) {
		pw.println("if (" + context.getFieldName() + " == null) {");
		pw.println("this." + context.getFieldName() + " = " + instanceName + "." + MethodHelper.toGetter(context.getFieldName()) + ";");
		pw.print("}");
		if (context.getNestedElement() != null) {
			pw.println(" else {");
			pw.println("this." + context.getFieldName() + ".merge(" + instanceName + "." + MethodHelper.toGetter(context.getFieldName()) + ");");
			pw.println("}");
		} else {
			pw.println();
		}
	}

	@Override
	public void finish(TypeElement type) {
		pw.println("return this;");
		pw.println("}");
		pw.println();
	}

	@Override
	public ElementKind getSupportedType() {
		return ElementKind.METHOD;
	}
}