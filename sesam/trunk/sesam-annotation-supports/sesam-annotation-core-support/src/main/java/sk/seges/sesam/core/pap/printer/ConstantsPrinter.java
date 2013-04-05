package sk.seges.sesam.core.pap.printer;

import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredTypeValue;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

public class ConstantsPrinter {

	private final FormattedPrintWriter pw;
	private final MutableProcessingEnvironment processingEnv;
	
	public ConstantsPrinter(FormattedPrintWriter pw, MutableProcessingEnvironment processingEnv) {
		this.pw = pw;
		this.processingEnv = processingEnv;
	}
	
	public void copyConstants(TypeElement element) {
		List<VariableElement> fields = ElementFilter.fieldsIn(element.getEnclosedElements());
		
		for (VariableElement field: fields) {
			if (field.getModifiers().contains(Modifier.STATIC) && field.getModifiers().contains(Modifier.FINAL)) {
				MutableDeclaredTypeValue constantValue = processingEnv.getTypeUtils().getDeclaredValue(
						(MutableDeclaredType) processingEnv.getTypeUtils().toMutableType(field.asType()), field.getConstantValue());
				pw.println("public static final ", field.asType(), " " + field.getSimpleName().toString() + " = ", constantValue, ";");
				pw.println();
			}
		}
	}
}