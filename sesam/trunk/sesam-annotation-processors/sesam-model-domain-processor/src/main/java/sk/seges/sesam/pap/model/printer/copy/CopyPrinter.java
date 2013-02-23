package sk.seges.sesam.pap.model.printer.copy;

import java.util.ArrayList;
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
		
		List<AnnotationMirror> supportedAnnotations = new ArrayList<AnnotationMirror>();

		add(supportedAnnotations, methodCopyAccessor.getSupportedAnnotations(context.getDomainMethod()));
		add(supportedAnnotations, methodCopyAccessor.getSupportedAnnotations(MethodHelper.getField(context.getDomainMethod())));
		add(supportedAnnotations, methodCopyAccessor.getSupportedAnnotations(context.getDtoMethod()));
		
		add(supportedAnnotations, typeCopyAccessor.getSupportedAnnotations(context.getDomainMethod()));
		add(supportedAnnotations, typeCopyAccessor.getSupportedAnnotations(MethodHelper.getField(context.getDomainMethod())));
		add(supportedAnnotations, typeCopyAccessor.getSupportedAnnotations(context.getDtoMethod()));

		return supportedAnnotations;
	}
	
	private void add(List<AnnotationMirror> annotations, List<AnnotationMirror> copiedAnnotations) {
		for (AnnotationMirror annotation: copiedAnnotations) {
			if (!annotations.contains(annotation)) {
				annotations.add(annotation);
			}
		}
	}
}