package sk.seges.sesam.pap.model.printer.converter;

import sk.seges.sesam.core.pap.model.mutable.api.MutableArrayTypeValue;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredTypeValue;
import sk.seges.sesam.core.pap.model.mutable.api.MutableReferenceType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableReferenceTypeValue;
import sk.seges.sesam.core.pap.model.mutable.api.MutableType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeValue;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

public class ParameterUsagePrinter {

	private final FormattedPrintWriter pw;

	public ParameterUsagePrinter(FormattedPrintWriter pw) {
		this.pw = pw;
	}

	public void printReferenceDeclaration(MutableType parameterType) {
		if (parameterType instanceof MutableReferenceType) {
			if (((MutableReferenceType) parameterType).getReference() != null) {
				String parameterName = ((MutableReferenceType) parameterType).toString();

				if (parameterName != null && parameterName.length() > 0 && !((MutableReferenceType) parameterType).isInline()) {
					MutableTypeValue reference = ((MutableReferenceType) parameterType).getReference();
					if (reference instanceof MutableArrayTypeValue) {
						pw.print(((MutableArrayTypeValue) reference).asType());
					} else if (reference instanceof MutableDeclaredTypeValue) {
						pw.print(((MutableDeclaredTypeValue) reference).asType());
					} else if (reference instanceof MutableReferenceTypeValue) {
						pw.print(((MutableReferenceTypeValue) reference).asType());
					}

					pw.print(" ", ((MutableReferenceType) parameterType).toString(), " = ");
					pw.println(((MutableReferenceType) parameterType).getReference(), ";");
				}
			}
		}
	}
}
