package sk.seges.sesam.pap.model.printer.hashcode;

import java.util.Arrays;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.model.api.ArrayNamedType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.ProcessorContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.printer.AbstractElementPrinter;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;

/**
 * {@link HashCodePrinter} prints hashcode method based on the fields defined in the class.
 * Hashcode method tries to detect endless loops by checking whether the hashCode method isn't 
 * already processed by setting a <i>processingEquals</i> flag. When infinite loops is detected
 * no referenced entities are processed for hashCode purposes.
 * <pre>
 * This is used when entity A has reference to the entity B and entity B has reference to the entity A.
 * Then infinite loops can occur in the hashCode method.
 * </pre>
 * @author Peter Simun
 *
 */
public class HashCodePrinter extends AbstractElementPrinter {

	private ProcessingEnvironment processingEnv;
	private EntityResolver entityResolver;

	public HashCodePrinter(EntityResolver entityResolver, ProcessingEnvironment processingEnv, FormattedPrintWriter pw) {
		super(pw);
		this.processingEnv = processingEnv;
		this.entityResolver = entityResolver;
	}

	/**
	 * Prints the definition of the hashCode method with the initial prechecks
	 */
	@Override
	public void initialize(ConfigurationTypeElement configurationTypeElement, NamedType outputName) {
		
		pw.println("private boolean processingHashCode = false;");
		pw.println("");
		pw.println("@Override");
		pw.println("public int hashCode() {");
		pw.println("final int prime = 31;");
		pw.println("int result = 1;");
	}

	/**
	 * Enclose hashCode method
	 */
	@Override
	public void finish(ConfigurationTypeElement configurationTypeElement) {
		
		pw.println("return result;");
		pw.println("}");
	}
	
	/**
	 * Is executed for every field in the class and prints the logic based on the type (primitive types, declared types, etc.).
	 */
	@Override
	public void print(ProcessorContext context) {

		if (entityResolver.isIdMethod(context.getMethod())) {
			//TODO Not true
			//IDs are not part of the hashCode
			return;
		}

		if (context.getFieldType() instanceof ArrayNamedType) {
			pw.println("if (!processingHashCode) {");
			pw.println("processingHashCode = true;");
			pw.println("result = prime * result + ", Arrays.class, ".hashCode(" + context.getFieldName() + ");");
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
			pw.println("result = prime * result + ", Arrays.class, ".hashCode(" + context.getFieldName() + ");");
			pw.println("processingHashCode = false;");
			pw.println("}");
			return;
		}
	}		
}