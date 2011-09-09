package sk.seges.sesam.pap.model.printer.equals;

import java.util.Arrays;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.core.pap.model.api.ArrayNamedType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.TransferObjectProcessor;
import sk.seges.sesam.pap.model.context.api.ProcessorContext;
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

	private ProcessingEnvironment processingEnv;
	private EntityResolver entityResolver;
	
	public EqualsPrinter(EntityResolver entityResolver, ProcessingEnvironment processingEnv, FormattedPrintWriter pw) {
		super(pw);
		this.processingEnv = processingEnv;
		this.entityResolver = entityResolver;
	}

	/**
	 * Prints the definition of the equals method with the initial prechecks
	 */
	@Override
	public void initialize(ConfigurationTypeElement configurationTypeElement, NamedType outputName) {

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
		
		NamedType targetClassName = getTargetClassNames(configurationTypeElement)[0];
		
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
	
	protected NamedType[] getTargetClassNames(ConfigurationTypeElement configurationTypeElement) {

		return new NamedType[] {
				TransferObjectProcessor.getOutputClass(configurationTypeElement)
		};
	}

	/**
	 * Is executed for every field in the class and prints the logic based on the type (primitive types, declared types, etc.).
	 */
	@Override
	public void print(ProcessorContext context) {

		boolean idMethod = entityResolver.isIdMethod(context.getMethod());
//		boolean idMethod = TransferObjectHelper.isIdMethod(context.getMethod());
		
		if (idMethod) {
			//TODO That's not really true
			//id's are not interesting
			return;
		}
		
		if (context.getFieldType() instanceof ArrayNamedType) {
			pw.println("if (!processingEquals) {");
			pw.println("processingEquals = true;");
			pw.println("if (!" + Arrays.class.getCanonicalName() + ".equals(" + context.getFieldName() + ", other." + context.getFieldName() + ")) {");
			pw.println("processingEquals = false;");
			pw.println("return false");
			pw.println("} else {");
			pw.println("processingEquals = false;");
			pw.println("}");
			pw.println("}");
			return;
		}

		if (context.getFieldType().asType() == null) {
			if (idMethod) {
				pw.println("if (" + context.getFieldName() + " != null && other." + context.getFieldName() + " != null && " + context.getFieldName() + ".equals(other." + context.getFieldName() + "))");
				pw.println("	return true;");
			} else {
				pw.println("if (" + context.getFieldName() + " == null) {");
				pw.println("if (other." + context.getFieldName() + " != null)");
				pw.println("	return false;");
				pw.println("} else if (!" + context.getFieldName() + ".equals(other." + context.getFieldName() + "))");
				pw.println("	return false;");
			}
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
			if (idMethod) {
				pw.println("if (" + context.getFieldName() + " == other." + context.getFieldName() + ")");
				pw.println("	return true;");
			} else {
				pw.println("if (" + context.getFieldName() + " != other." + context.getFieldName() + ")");
				pw.println("	return false;");
			}
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
				if (idMethod) {
					pw.println("if (" + context.getFieldName() + " == other." + context.getFieldName() + ")");
					pw.println("	return true;");
				} else {
					pw.println("if (" + context.getFieldName() + " != other." + context.getFieldName() + ")");
					pw.println("	return false;");
				}
				return;
			case CLASS:
			case INTERFACE:
				if (idMethod) {
					pw.println("if (" + context.getFieldName() + " != null && other." + context.getFieldName() + " != null && " + context.getFieldName() + ".equals(other." + context.getFieldName() + "))");
					pw.println("	return true;");
				} else {
					pw.println("if (" + context.getFieldName() + " == null) {");
					pw.println("if (other." + context.getFieldName() + " != null)");
					pw.println("	return false;");
					pw.println("} else { ");
					pw.println("if (!processingEquals) {");
					pw.println("processingEquals = true;");
					pw.println("if (!" + context.getFieldName() + ".equals(other." + context.getFieldName() + ")) {");
					pw.println("processingEquals = false;");
					pw.println("return false;");
					pw.println("} else {");
					pw.println("processingEquals = false;");
					pw.println("}");
					pw.println("}");
					pw.println("}");
				}
				return;
			}
		case ARRAY:
			pw.println("if (!processingEquals) {");
			pw.println("processingEquals = true;");
			pw.println("if (!" + Arrays.class.getCanonicalName() + ".equals(" + context.getFieldName() + ", other." + context.getFieldName() + ")) {");
			pw.println("processingEquals = false;");
			pw.println("return false");
			pw.println("} else {");
			pw.println("processingEquals = false;");
			pw.println("}");
			pw.println("}");
			return;
		}
	}		
}