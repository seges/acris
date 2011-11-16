package sk.seges.corpis.pap.model.hibernate.printer.method;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ExecutableElement;

import sk.seges.corpis.pap.model.hibernate.resolver.HibernateEntityResolver;
import sk.seges.corpis.shared.converter.utils.ConverterUtils;
import sk.seges.sesam.core.pap.model.PathResolver;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.hibernate.resolver.HibernateParameterResolver;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.ElementHolderTypeConverter;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.printer.method.CopyToDtoMethodPrinter;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;

public class HibernateCopyToDtoMethodPrinter extends CopyToDtoMethodPrinter {

	private final HibernateEntityResolver entityResolver;
	
	public HibernateCopyToDtoMethodPrinter(ConverterProviderPrinter converterProviderPrinter, ElementHolderTypeConverter elementHolderTypeConverter,
			HibernateEntityResolver entityResolver, ParametersResolver parametersResolver, RoundEnvironment roundEnv,
			TransferObjectProcessingEnvironment processingEnv) {
		super(converterProviderPrinter, elementHolderTypeConverter, parametersResolver, roundEnv, processingEnv);
		this.entityResolver = entityResolver;
	}

	@Override
    protected boolean printInitialized(ExecutableElement domainMethod, PathResolver domainPathResolver, FormattedPrintWriter pw) {
    	if (entityResolver.isLazyReference(domainMethod)) {
    		pw.println("if (", ConverterUtils.class,".convertResult(" + HibernateParameterResolver.TRANSACTION_PROPAGATION_NAME + ", \"" + domainPathResolver.getPath() + "\")) {");
    		return true;
    	}
    	
    	return false;
	}
}