package sk.seges.sesam.pap.model;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.printer.ConstantsPrinter;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.printer.accessors.AccessorsPrinter;
import sk.seges.sesam.pap.model.printer.api.TransferObjectElementPrinter;
import sk.seges.sesam.pap.model.printer.constructor.EmptyConstructorPrinter;
import sk.seges.sesam.pap.model.printer.constructor.EnumeratedConstructorBodyPrinter;
import sk.seges.sesam.pap.model.printer.constructor.EnumeratedConstructorDefinitionPrinter;
import sk.seges.sesam.pap.model.printer.equals.EqualsPrinter;
import sk.seges.sesam.pap.model.printer.field.FieldPrinter;
import sk.seges.sesam.pap.model.printer.hashcode.HashCodePrinter;
import sk.seges.sesam.pap.model.provider.ClasspathConfigurationProvider;
import sk.seges.sesam.pap.model.provider.api.ConfigurationProvider;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class TransferObjectProcessor extends AbstractTransferProcessor {
	
	protected ConfigurationProvider[] getConfigurationProviders() {
		return new ConfigurationProvider[] {
				new ClasspathConfigurationProvider(getClassPathTypes(), getEnvironmentContext())
		};
	}
	
	@Override
	protected boolean checkPreconditions(ProcessorContext context, boolean alreadyExists) {
		//TODO context.getOutputClass is DTO, so use it!
		ConfigurationTypeElement configurationTypeElement = getConfigurationElement(context);
		if (!configurationTypeElement.getDto().isGenerated()) {
			return false;
		}
		return super.checkPreconditions(context, alreadyExists);
	}

	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] {
				getConfigurationElement(context).getDto()
		};
	}
	
	@Override
	protected void processElement(ProcessorContext context) {
		new ConstantsPrinter(context.getPrintWriter(), processingEnv).copyConstants(context.getTypeElement());
		super.processElement(context);
	}
	
	@Override
	protected TransferObjectElementPrinter[] getElementPrinters(FormattedPrintWriter pw) {
		return new TransferObjectElementPrinter[] {
				new FieldPrinter(pw),
				new EmptyConstructorPrinter(pw),
				new EnumeratedConstructorDefinitionPrinter(pw),
				new EnumeratedConstructorBodyPrinter(pw),
				new AccessorsPrinter(processingEnv, pw),
				new EqualsPrinter(getEntityResolver(), processingEnv, pw),
				new HashCodePrinter(getEntityResolver(), processingEnv, pw)
		};
	}
}