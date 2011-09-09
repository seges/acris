package sk.seges.sesam.pap.configuration.printer;

import java.io.PrintWriter;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.builder.NameTypesUtils;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.pap.configuration.model.ProcessorContext;
import sk.seges.sesam.pap.configuration.printer.api.ElementPrinter;

public class MergePrinter implements ElementPrinter {

	private PrintWriter pw;
	private String instanceName;
	private MethodHelper methodHelper;

	public MergePrinter(PrintWriter pw, ProcessingEnvironment pe) {
		this.pw = pw;
		methodHelper = new MethodHelper(pe, new NameTypesUtils(pe));
	}
	
	@Override
	public void initialize(TypeElement type, NamedType outputName) {
		instanceName = methodHelper.toField(outputName.getSimpleName());
		pw.println("public " + outputName.getSimpleName() + " merge(" + outputName.getSimpleName() + " " + instanceName + ") {");
		pw.println("if (" + instanceName + " == null) {");
		pw.println("return this;");
		pw.println("}");
	}

	@Override
	public void print(ProcessorContext context) {
		pw.println("if (" + context.getFieldName() + " == null) {");
		pw.println("this." + context.getFieldName() + " = " + instanceName + "." + methodHelper.toGetter(context.getFieldName()) + ";");
		pw.println("}");
	}

	@Override
	public void finish(TypeElement type) {
		pw.println("return this;");
		pw.println("}");
	}
}