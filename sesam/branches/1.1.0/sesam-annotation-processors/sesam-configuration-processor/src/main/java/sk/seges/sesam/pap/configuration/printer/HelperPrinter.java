package sk.seges.sesam.pap.configuration.printer;

import java.io.PrintStream;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.configuration.model.SettingsContext;
import sk.seges.sesam.pap.configuration.printer.api.SettingsElementPrinter;

public class HelperPrinter extends AbstractSettingsElementPrinter implements SettingsElementPrinter {

	private FormattedPrintWriter pw;
	
	public HelperPrinter(FormattedPrintWriter pw, ProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.pw = pw;
	}

	@Override
	public void initialize(TypeElement type, NamedType outputName) {
		pw.println("public void printHelp(", PrintStream.class, " out) {");
	}

	@Override
	public void print(SettingsContext context) {
		if (context.getNestedElement() != null) {
			pw.println(context.getFieldName() + ".printHelp(out);");
		} else {
			pw.println("out.println(\"" + alignText(context.getParameter().name()) + " " + context.getParameter().description() + "\");");
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
}
