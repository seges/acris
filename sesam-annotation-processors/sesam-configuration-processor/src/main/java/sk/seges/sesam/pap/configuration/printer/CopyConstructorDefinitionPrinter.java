package sk.seges.sesam.pap.configuration.printer;

import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.configuration.model.setting.SettingsContext;

public class CopyConstructorDefinitionPrinter extends AbstractSettingsElementPrinter {

	private FormattedPrintWriter pw;
	private String instanceName;
	
	public CopyConstructorDefinitionPrinter(FormattedPrintWriter pw, MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.pw = pw;
	}

	@Override
	public void initialize(TypeElement type, MutableDeclaredType outputName) {
		instanceName = MethodHelper.toField(outputName.getSimpleName());
		pw.println("public " + outputName.getSimpleName() + "(" + outputName.getSimpleName() + " " + instanceName + ") {");
	}

	@Override
	public void print(SettingsContext context) {
		pw.println("this." + context.getFieldName() + " = " + instanceName + "." + MethodHelper.toGetter(context.getFieldName()) + ";");
	}

	@Override
	public void finish(TypeElement type) {
		pw.println("}");
		pw.println();
	}

}
