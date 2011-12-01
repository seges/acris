package sk.seges.sesam.pap.configuration.printer;

import java.io.PrintStream;
import java.util.List;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.configuration.model.setting.SettingsContext;

public class HelperPrinter extends AbstractSettingsElementPrinter {

	private FormattedPrintWriter pw;
	
	public HelperPrinter(FormattedPrintWriter pw, MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.pw = pw;
	}

	@Override
	public void initialize(TypeElement type, MutableDeclaredType outputName) {
		pw.println("public void printHelp(", PrintStream.class, " out) {");
		pw.println("printHelp(out, \"\", null);");
		pw.println("}");
		pw.println();
		pw.println("public void printHelp(", PrintStream.class, " out, ", String.class," prefix, ", String.class, " name) {");
	}

	@Override
	public void print(SettingsContext context) {
		if (context.getNestedElement() != null) {
			pw.println(context.getFieldName() + ".printHelp(out, \"" + context.getPrefix() + "\", \"" + context.getParameterDescription() + "\");");
		} else {
			pw.print("out.println(prefix + \"" + alignText(context.getParameterName()) + " " + context.getParameterDescription() + "\"");
			pw.print(" + (name != null ? \"for \" + name : \"\")");
			pw.println(");");
			if (context.getMethod().getReturnType().getKind().equals(TypeKind.DECLARED) && 
					((DeclaredType) context.getMethod().getReturnType()).asElement().getKind().equals(ElementKind.ENUM)) {

				pw.print("out.println(\"" + alignText("") + " Possible values: \"");

				TypeElement enumType = (TypeElement) ((DeclaredType) context.getMethod().getReturnType()).asElement();
 				List<VariableElement> fields = ElementFilter.fieldsIn(enumType.getEnclosedElements());
 				int i = 0;
 				for (VariableElement field: fields) {
 					//TODO Bug in the eclipse JDT APT [https://bugs.eclipse.org/bugs/show_bug.cgi?id=357494]
 					if (field.asType().equals(enumType.asType())) {
//	 				if (field.getKind().equals(ElementKind.ENUM_CONSTANT)) {
	 						if (i > 0) {
	 	 						pw.print(" + \",\" ");
	 						}
	 						pw.print(" + ", enumType, "." + field + ".toString()");
	 						i++;
 					}
 				}
				pw.println(");");
			}
		}
	}

	private String alignText(String text) {
		String result = "";
		for (int i = text.length(); i < 30; i++) {
			result += " ";
		}
		return text + result;
	}
	
	@Override
	public void finish(TypeElement type) {
		pw.println("}");
	}

	@Override
	public ElementKind getSupportedType() {
		return ElementKind.METHOD;
	}
}