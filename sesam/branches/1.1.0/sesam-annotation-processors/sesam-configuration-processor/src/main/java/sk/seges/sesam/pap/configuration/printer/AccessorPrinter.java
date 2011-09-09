package sk.seges.sesam.pap.configuration.printer;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.builder.NameTypesUtils;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.configuration.model.ProcessorContext;
import sk.seges.sesam.pap.configuration.printer.api.ElementPrinter;

public class AccessorPrinter extends AbstractElementPrinter implements ElementPrinter {

	private FormattedPrintWriter pw;
	private MethodHelper methodHelper;
	
	public AccessorPrinter(FormattedPrintWriter pw, ProcessingEnvironment pe) {
		super(pe);
		this.pw = pw;
		methodHelper = new MethodHelper(pe, new NameTypesUtils(pe));
	}
	
	@Override
	public void initialize(TypeElement type, NamedType outputName) {}

	@Override
	public void print(ProcessorContext context) {
		String fieldName = context.getFieldName();
		pw.println("public ", unboxType(context.getMethod().getReturnType()), " " + methodHelper.toGetter(fieldName) + " {");
		pw.println("return " + fieldName + ";");
		pw.println("}");
		pw.println("");
		pw.println("public void " + methodHelper.toSetter(fieldName) + "(", unboxType(context.getMethod().getReturnType()), " " + fieldName + ") {");
		pw.println("this." + fieldName + " = " + fieldName + ";");
		pw.println("}");
		pw.println("");
	}

	@Override
	public void finish(TypeElement type) {}
}