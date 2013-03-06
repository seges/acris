package sk.seges.corpis.pap.model.hibernate.printer.method;

import java.sql.Blob;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import org.hibernate.Hibernate;

import sk.seges.corpis.pap.model.hibernate.resolver.HibernateEntityResolver;
import sk.seges.corpis.shared.converter.utils.ConverterUtils;
import sk.seges.sesam.core.pap.model.PathResolver;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.hibernate.resolver.HibernateParameterResolverDelegate;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.ElementHolderTypeConverter;
import sk.seges.sesam.pap.model.printer.api.TransferObjectElementPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.printer.method.CopyToDtoMethodPrinter;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;

public class HibernateCopyToDtoMethodPrinter extends CopyToDtoMethodPrinter {

	private final HibernateEntityResolver entityResolver;
	
	public HibernateCopyToDtoMethodPrinter(ConverterProviderPrinter converterProviderPrinter, ElementHolderTypeConverter elementHolderTypeConverter,
			HibernateEntityResolver entityResolver, ConverterConstructorParametersResolverProvider parametersResolverProvider, RoundEnvironment roundEnv,
			TransferObjectProcessingEnvironment processingEnv) {
		super(converterProviderPrinter, elementHolderTypeConverter, parametersResolverProvider, entityResolver, roundEnv, processingEnv);
		this.entityResolver = entityResolver;
	}

	@Override
    protected boolean printInitialized(ExecutableElement domainMethod, PathResolver domainPathResolver, FormattedPrintWriter pw) {
    	if (entityResolver.isLazyReference(domainMethod)) {
    		//Special fix for blobs :-(
    		TypeElement blobElement = processingEnv.getElementUtils().getTypeElement(Blob.class.getCanonicalName());
    		if (processingEnv.getTypeUtils().implementsType(processingEnv.getTypeUtils().toMutableType(domainMethod.getReturnType()),
    														processingEnv.getTypeUtils().toMutableType(blobElement.asType()))) {
        		pw.println("if (", ConverterUtils.class,".convertResult(" + HibernateParameterResolverDelegate.TRANSACTION_PROPAGATION_NAME + ", \"" + domainPathResolver.getPath() + "\")) {");
    		} else {
	    		pw.println("if (", Hibernate.class,".isInitialized(" + TransferObjectElementPrinter.DOMAIN_NAME + "." + MethodHelper.toGetter(domainPathResolver.getPath()) + ")) {");
   		}
    		return true;
    	}
    	
    	return false;
	}
}