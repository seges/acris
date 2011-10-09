package sk.seges.sesam.pap.model;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.printer.accessors.AccessorsPrinter;
import sk.seges.sesam.pap.model.printer.api.TransferObjectElementPrinter;
import sk.seges.sesam.pap.model.printer.constructor.EmptyConstructorPrinter;
import sk.seges.sesam.pap.model.printer.constructor.EnumeratedConstructorBodyPrinter;
import sk.seges.sesam.pap.model.printer.constructor.EnumeratedConstructorDefinitionPrinter;
import sk.seges.sesam.pap.model.printer.equals.EqualsPrinter;
import sk.seges.sesam.pap.model.printer.field.FieldPrinter;
import sk.seges.sesam.pap.model.printer.hashcode.HashCodePrinter;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class TransferObjectProcessor extends AbstractTransferProcessor {
	
	@Override
	protected void printAnnotations(ProcessorContext context) {
		
		FormattedPrintWriter pw = context.getPrintWriter();
		
		pw.println("@SuppressWarnings(\"serial\")");
		//TODO context.getOutputClass is DTO - get configuration element from there
		ConfigurationTypeElement configurationTypeElement = new ConfigurationTypeElement(context.getTypeElement(), processingEnv, roundEnv);
		
		pw.print("@", TransferObjectMapping.class, "(");

		pw.println("dtoClass = " + getOutputClass(configurationTypeElement).getSimpleName() + ".class,");
		pw.println("		domainClassName = \"" + configurationTypeElement.getDomain().getQualifiedName().toString() + "\", ");
		pw.println("		configurationClassName = \"" + context.getTypeElement().toString() + "\", ");
		pw.print("		converterClassName = \"");
		pw.print(configurationTypeElement.getConverter().getCanonicalName());
		pw.print("\"");
		pw.println(")");
		
		super.printAnnotations(context);
	}
	
	@Override
	protected boolean checkPreconditions(ProcessorContext context, boolean alreadyExists) {
		//TODO context.getOutputClass is DTO, so use it!
		ConfigurationTypeElement configurationTypeElement = new ConfigurationTypeElement(context.getTypeElement(), processingEnv, roundEnv);
		if (!configurationTypeElement.getDto().isGenerated()) {
			return false;
		}
		return super.checkPreconditions(context, alreadyExists);
	}

	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] {
				new ConfigurationTypeElement(context.getTypeElement(), processingEnv, roundEnv, getConfigurationProviders()).getDto()
		};
	}
	
	@Override
	protected TransferObjectElementPrinter[] getElementPrinters(FormattedPrintWriter pw) {
		return new TransferObjectElementPrinter[] {
				new FieldPrinter(pw),
				new EmptyConstructorPrinter(pw),
				new EnumeratedConstructorDefinitionPrinter(pw),
				new EnumeratedConstructorBodyPrinter(pw),
				new AccessorsPrinter(pw),
				new EqualsPrinter(getEntityResolver(), processingEnv, pw),
				new HashCodePrinter(getEntityResolver(), processingEnv, pw)
		};
	}
	
	public static MutableDeclaredType getOutputClass(ConfigurationTypeElement configurationTypeElement) {	
		return configurationTypeElement.getDto();
	}
}