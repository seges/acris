package sk.seges.sesam.pap.model.printer.equals;

import java.util.Arrays;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.TransferObjectProcessor;
import sk.seges.sesam.pap.model.context.api.TransferObjectContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.printer.AbstractElementPrinter;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;

/**
 * {@link EqualsPrinter} prints equals method based on the fields defined in the class.
 * Equals method tries to detect endless loops by checking whether the equals method isn't 
 * already processed by setting a <i>processingEquals</i> flag. When infinite loops is detected
 * no referenced entities are processed for equals purposes.
 * <pre>
 * This is used when entity A has reference to the entity B and entity B has reference to the entity A.
 * Then infinite loops can occur in the equals method.
 * </pre>
 * @author Peter Simun
 *
 */
public class EqualsPrinter extends AbstractElementPrinter {

	private final ProcessingEnvironment processingEnv;
	private final EntityResolver entityResolver;
	
	public EqualsPrinter(EntityResolver entityResolver, ProcessingEnvironment processingEnv, FormattedPrintWriter pw) {
		super(pw);
		this.processingEnv = processingEnv;
		this.entityResolver = entityResolver;
	}

	/**
	 * Prints the definition of the equals method with the initial prechecks
	 */
	@Override
	public void initialize(ConfigurationTypeElement configurationTypeElement, MutableDeclaredType outputName) {

		pw.println("private boolean processingEquals = false;");
		pw.println("");
		pw.println("@Override");
		pw.println("public boolean equals(Object obj) {");
		pw.println("if (this == obj)");
		pw.println("	return true;");
		pw.println("if (obj == null)");
		pw.println("	return false;");
		pw.println("if (getClass() != obj.getClass())");
		pw.println("	return false;");
		
		MutableDeclaredType targetClassName = getTargetClassNames(configurationTypeElement)[0];
		
		pw.println(targetClassName.toString(ClassSerializer.SIMPLE, true) + " other = (" + 
				targetClassName.toString(ClassSerializer.SIMPLE, true) + ") obj;");
	}

	/**
	 * Enclose equals method
	 */
	@Override
	public void finish(ConfigurationTypeElement configurationTypeElement) {

		pw.println("return true;");
		pw.println("}");
		pw.println();
	}
	
	protected MutableDeclaredType[] getTargetClassNames(ConfigurationTypeElement configurationTypeElement) {

		return new MutableDeclaredType[] {
				TransferObjectProcessor.getOutputClass(configurationTypeElement)
		};
	}

	/**
	 * Is executed for every field in the class and prints the logic based on the type (primitive types, declared types, etc.).
	 */
	@Override
	public void print(TransferObjectContext context) {

		boolean idMethod = entityResolver.isIdMethod(context.getDtoMethod());
		
		if (idMethod) {
			//TODO That's not really true
			//id's are not interesting
			return;
		}
		
		switch (context.getDtoFieldType().getKind()) {
		case ENUM:
		case PRIMITIVE:
			if (idMethod) {
				pw.println("if (" + context.getDtoFieldName() + " == other." + context.getDtoFieldName() + ")");
				pw.println("	return true;");
			} else {
				pw.println("if (" + context.getDtoFieldName() + " != other." + context.getDtoFieldName() + ")");
				pw.println("	return false;");
			}
			return;
		case CLASS:
		case INTERFACE:
			if (idMethod) {
				pw.println("if (" + context.getDtoFieldName() + " != null && other." + context.getDtoFieldName() + " != null && " + context.getDtoFieldName() + ".equals(other." + context.getDtoFieldName() + "))");
				pw.println("	return true;");
			} else {
				pw.println("if (" + context.getDtoFieldName() + " == null) {");
				pw.println("if (other." + context.getDtoFieldName() + " != null)");
				pw.println("	return false;");
				pw.println("} else { ");
				pw.println("if (!processingEquals) {");
				pw.println("processingEquals = true;");
				pw.println("if (!" + context.getDtoFieldName() + ".equals(other." + context.getDtoFieldName() + ")) {");
				pw.println("processingEquals = false;");
				pw.println("return false;");
				pw.println("} else {");
				pw.println("processingEquals = false;");
				pw.println("}");
				pw.println("}");
				pw.println("}");
			}
			return;
		case ARRAY:
			pw.println("if (!processingEquals) {");
			pw.println("processingEquals = true;");
			pw.println("if (!" + Arrays.class.getCanonicalName() + ".equals(" + context.getDtoFieldName() + ", other." + context.getDtoFieldName() + ")) {");
			pw.println("processingEquals = false;");
			pw.println("return false");
			pw.println("} else {");
			pw.println("processingEquals = false;");
			pw.println("}");
			pw.println("}");
			return;
		case WILDCARD:
		case VOID:
		case TYPEVAR:
			processingEnv.getMessager().printMessage(Kind.WARNING, "[WARNING] Unsupported type " + context.getDtoFieldName() + " (" + context.getDtoFieldType().getKind() + ") in the " + 
					context.getConfigurationTypeElement().asElement(), context.getConfigurationTypeElement().asElement());
			return;
		}
	}		
}