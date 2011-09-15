package sk.seges.sesam.pap.configuration.printer;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.builder.NameTypeUtils;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.configuration.model.SettingsContext;
import sk.seges.sesam.pap.configuration.printer.api.SettingsElementPrinter;

public class CopyConstructorDefinitionPrinter implements SettingsElementPrinter {

	private FormattedPrintWriter pw;
	private String instanceName;
	private MethodHelper methodHelper;
	
	public CopyConstructorDefinitionPrinter(FormattedPrintWriter pw, ProcessingEnvironment pe) {
		this.pw = pw;
		methodHelper = new MethodHelper(pe, new NameTypeUtils(pe));
	}

	@Override
	public void initialize(TypeElement type, NamedType outputName) {
		instanceName = methodHelper.toField(outputName.getSimpleName());
		pw.println("public " + outputName.getSimpleName() + "(" + outputName.getSimpleName() + " " + instanceName + ") {");
	}

	@Override
	public void print(SettingsContext context) {
		pw.println("this." + context.getFieldName() + " = " + instanceName + "." + methodHelper.toGetter(context.getFieldName()) + ";");
	}

	@Override
	public void finish(TypeElement type) {
		pw.println("}");
		pw.println();
	}

}
