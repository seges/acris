package sk.seges.sesam.pap.model.printer.copy;

import java.util.List;

import javax.lang.model.element.AnnotationMirror;

import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.accessor.CopyAccessor;
import sk.seges.sesam.pap.model.context.api.TransferObjectContext;
import sk.seges.sesam.pap.model.printer.AbstractElementPrinter;

public abstract class CopyPrinter extends AbstractElementPrinter {

	protected final MutableProcessingEnvironment processingEnv;

	protected CopyAccessor typeCopyAccessor;
	
	public CopyPrinter(MutableProcessingEnvironment processingEnv, FormattedPrintWriter pw) {
		super(pw);
		this.processingEnv = processingEnv;
	}

	protected List<AnnotationMirror> getSupportedAnnotations(TransferObjectContext context) {
		
		if (typeCopyAccessor == null) {
			typeCopyAccessor = new CopyAccessor(context.getConfigurationTypeElement().asConfigurationElement(), processingEnv);
		}
		
		CopyAccessor methodCopyAccessor = new CopyAccessor(context.getDtoMethod(), processingEnv);
		
		List<AnnotationMirror> supportedAnnotations = typeCopyAccessor.getSupportedAnnotations(context.getDomainMethod());
		supportedAnnotations.addAll(typeCopyAccessor.getSupportedAnnotations(MethodHelper.getField(context.getDomainMethod())));

		supportedAnnotations.addAll(methodCopyAccessor.getSupportedAnnotations(context.getDomainMethod()));
		supportedAnnotations.addAll(methodCopyAccessor.getSupportedAnnotations(MethodHelper.getField(context.getDomainMethod())));

		supportedAnnotations.addAll(typeCopyAccessor.getSupportedAnnotations(context.getDtoMethod()));
		supportedAnnotations.addAll(methodCopyAccessor.getSupportedAnnotations(context.getDtoMethod()));

		return supportedAnnotations;
	}
}