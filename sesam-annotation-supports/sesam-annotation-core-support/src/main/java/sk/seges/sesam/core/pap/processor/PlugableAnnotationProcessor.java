package sk.seges.sesam.core.pap.processor;

import java.io.OutputStream;
import java.util.ArrayList;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.printer.ImportPrinter;
import sk.seges.sesam.core.pap.utils.TypeParametersSupport;
import sk.seges.sesam.core.pap.writer.HierarchyPrintWriter;

public abstract class PlugableAnnotationProcessor extends AbstractProcessor {

	protected TypeParametersSupport typeParametersSupport;
	protected MutableProcessingEnvironment processingEnv;

	protected final String lineSeparator = java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"));

	protected PlugableAnnotationProcessor() {}
			
	@Override
	public synchronized void init(ProcessingEnvironment pe) {
		super.init(pe);
		this.processingEnv = new MutableProcessingEnvironment(pe, this.getClass());
		this.typeParametersSupport = new TypeParametersSupport(processingEnv);
	}
		
	protected HierarchyPrintWriter initializePrintWriter(OutputStream os) {
		return new HierarchyPrintWriter(processingEnv.getPrintSupport(), processingEnv, os, new ArrayList<MutableDeclaredType>());
	}

	protected ImportPrinter initializeImportPrinter(HierarchyPrintWriter hierarchyPrintWriter, String packageName) {
		return new ImportPrinter(hierarchyPrintWriter, processingEnv);
	}
}