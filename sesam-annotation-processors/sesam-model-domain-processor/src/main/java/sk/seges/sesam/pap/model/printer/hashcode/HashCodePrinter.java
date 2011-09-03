package sk.seges.sesam.pap.model.printer.hashcode;

import java.io.PrintWriter;
import java.util.Arrays;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.model.api.ArrayNamedType;
import sk.seges.sesam.pap.model.context.api.ProcessorContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.printer.AbstractElementPrinter;
import sk.seges.sesam.pap.model.utils.TransferObjectHelper;

public class HashCodePrinter extends AbstractElementPrinter {

	private ProcessingEnvironment processingEnv;
	
	public HashCodePrinter(ProcessingEnvironment processingEnv, PrintWriter pw) {
		super(pw);
		this.processingEnv = processingEnv;
	}

	@Override
	public void initialize(ConfigurationTypeElement configuratioTypeElement) {
		pw.println("private boolean processingHashCode = false;");
		pw.println("");
		pw.println("@Override");
		pw.println("public int hashCode() {");
		pw.println("final int prime = 31;");
		pw.println("int result = 1;");
	}

	@Override
	public void finish(ConfigurationTypeElement configuratioTypeElement) {
		pw.println("return result;");
		pw.println("}");
	}
	
	@Override
	public void print(ProcessorContext context) {

		if (TransferObjectHelper.isIdMethod(context.getMethod())) {
			//TODO Not true
			//IDs are not part of the hashCode
			return;
		}

		if (context.getFieldType() instanceof ArrayNamedType) {
			pw.println("if (!processingHashCode) {");
			pw.println("processingHashCode = true;");
			pw.println("result = prime * result + " + Arrays.class.getCanonicalName() + ".hashCode(" + context.getFieldName() + ");");
			pw.println("processingHashCode = false;");
			pw.println("}");
			return;
		}

		if (context.getFieldType().asType() == null) {
			//TODO - what's this case?
			pw.println("result = prime * result + ((" + context.getFieldName() + " == null) ? 0 : " + context.getFieldName() + ".hashCode());");
			return;
		}
		
		switch (context.getFieldType().asType().getKind()) {
		case BOOLEAN:
		case BYTE:
		case CHAR:
		case DOUBLE:
		case FLOAT:
		case INT:
		case LONG:
		case SHORT:
			pw.println("result = prime * result + " + context.getFieldName() + ";");
			return;
		case EXECUTABLE:
		case NONE:
		case NULL:
		case OTHER:
		case PACKAGE:
		case ERROR:
		case WILDCARD:
		case VOID:
		case TYPEVAR:
			processingEnv.getMessager().printMessage(Kind.WARNING, "[WARNING] Unsupported type " + context.getFieldName() + " (" + context.getFieldType().asType().getKind() + ") in the " + 
					context.getConfigurationTypeElement().asElement(), context.getConfigurationTypeElement().asElement());
			return;
		case DECLARED:
			Element element = ((DeclaredType)context.getFieldType().asType()).asElement();
			switch (element.getKind()) {
			case ENUM:
			case ENUM_CONSTANT:
				pw.println("result = prime * result + ((" + context.getFieldName() + " == null) ? 0 : " + context.getFieldName() + ".hashCode());");
				return;
			case CLASS:
			case INTERFACE:
				pw.println("if (!processingHashCode) {");
				pw.println("processingHashCode = true;");
				pw.println("result = prime * result + ((" + context.getFieldName() + " == null) ? 0 : " + context.getFieldName() + ".hashCode());");
				pw.println("processingHashCode = false;");
				pw.println("}");
				return;
			}
		case ARRAY:
			pw.println("if (!processingHashCode) {");
			pw.println("processingHashCode = true;");
			pw.println("result = prime * result + " + Arrays.class.getCanonicalName() + ".hashCode(" + context.getFieldName() + ");");
			pw.println("processingHashCode = false;");
			pw.println("}");
			return;
		}
	}		
}