package sk.seges.sesam.pap.model.printer.hashcode;

import java.util.Arrays;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.TransferObjectContext;
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
	public void initialize(ConfigurationTypeElement configurationTypeElement, MutableDeclaredType outputName) {
		
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
	public void print(TransferObjectContext context) {

		if (entityResolver.isIdMethod(context.getDtoMethod())) {
			//TODO Not true
			//IDs are not part of the hashCode
			return;
		}

		switch (context.getFieldType().getKind()) {
		case PRIMITIVE:
			pw.println("result = prime * result + " + context.getFieldName() + ";");
			return;
		case ENUM:
			pw.println("result = prime * result + ((" + context.getFieldName() + " == null) ? 0 : " + context.getFieldName() + ".hashCode());");
			return;
		case CLASS:
		case INTERFACE:
		case ANNOTATION_TYPE:
			pw.println("if (!processingHashCode) {");
			pw.println("processingHashCode = true;");
			pw.println("result = prime * result + ((" + context.getFieldName() + " == null) ? 0 : " + context.getFieldName() + ".hashCode());");
			pw.println("processingHashCode = false;");
			pw.println("}");
			return;
		case ARRAY:
			pw.println("if (!processingHashCode) {");
			pw.println("processingHashCode = true;");
			pw.println("result = prime * result + ", Arrays.class, ".hashCode(" + context.getFieldName() + ");");
			pw.println("processingHashCode = false;");
			pw.println("}");
			return;
		case WILDCARD:
		case VOID:
		case TYPEVAR:
			processingEnv.getMessager().printMessage(Kind.WARNING, "[WARNING] Unsupported type " + context.getFieldName() + " (" + context.getFieldType().getKind() + ") in the " + 
					context.getConfigurationTypeElement().asElement(), context.getConfigurationTypeElement().asElement());
			return;
		}
	}		
}