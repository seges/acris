package sk.seges.sesam.core.pap.writer;

import sk.seges.sesam.core.pap.model.mutable.api.MutableExecutableType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.printer.MethodPrinter;

public class MethodPrintWriter extends HierarchyPrintWriter {

	private final MutableExecutableType method;
	private final HierarchyPrintWriter parentPrintWriter;
	
	public MethodPrintWriter(MutableExecutableType method, MutableProcessingEnvironment processingEnv, HierarchyPrintWriter parentPrintWriter) {
		super(parentPrintWriter.getPrinterSupport(), processingEnv, parentPrintWriter.getUsedTypes());
		
		this.method = method;
		this.parentPrintWriter = parentPrintWriter;
	}

	protected void initializePrinter() {
		super.initializePrinter();

		setDefaultIdentLevel(parentPrintWriter.getOudentLevel());
		
		//TODO method annotations
		HierarchyPrintWriter methodPrintWriter = method.getPrintWriter(parentPrintWriter);

		HierarchyPrintWriter methodPrinter = new HierarchyPrintWriter(parentPrintWriter.getPrinterSupport(), processingEnv, parentPrintWriter.getUsedTypes());

		methodPrintWriter.addNestedPrinter(methodPrinter);
		methodPrinter.addNestedPrinter(new HierarchyPrintWriter(parentPrintWriter.getPrinterSupport(), processingEnv, parentPrintWriter.getUsedTypes()) {
			@Override
			public void flush() {
				println();
				new MethodPrinter(this, processingEnv).printMethodDefinition(method);
				super.flush();
			}
		});

		methodPrinter.println(" {");
		
		HierarchyPrintWriter bodyPrinter = new HierarchyPrintWriter(parentPrintWriter.getPrinterSupport(), processingEnv, parentPrintWriter.getUsedTypes());
		methodPrinter.addNestedPrinter(bodyPrinter);

		methodPrinter.println("}");
		
		methodPrintWriter.setCurrentPrinter(bodyPrinter);
	};
}