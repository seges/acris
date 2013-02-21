package sk.seges.sesam.core.pap.writer;

import sk.seges.sesam.core.pap.model.mutable.api.MutableExecutableType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.printer.AnnotationPrinter;
import sk.seges.sesam.core.pap.printer.MethodPrinter;

public class MethodPrintWriter extends HierarchyPrintWriter {

	private final MutableExecutableType method;
	
	public MethodPrintWriter(MutableExecutableType method, MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.method = method;
	}

	protected void initializePrinter() {
		super.initializePrinter();

		//TODO method annotations
		HierarchyPrintWriter methodPrintWriter = method.getPrintWriter();

		HierarchyPrintWriter methodPrinter = new HierarchyPrintWriter(processingEnv);

		methodPrintWriter.addNestedPrinter(methodPrinter);
		
		methodPrinter.addNestedPrinter(new HierarchyPrintWriter(processingEnv) {
			@Override
			public void flush() {
				new AnnotationPrinter(this, processingEnv).printMethodAnnotations(method);
				super.flush();
			}
		});

		methodPrinter.addNestedPrinter(new HierarchyPrintWriter(processingEnv) {
			@Override
			public void flush() {
				new MethodPrinter(this, processingEnv).printMethodDefinition(method);
				super.flush();
			}
		});

		methodPrinter.println(" {");
		
		HierarchyPrintWriter bodyPrinter = new HierarchyPrintWriter(processingEnv);
		methodPrinter.addNestedPrinter(bodyPrinter);

		methodPrinter.println("}");
		
		methodPrintWriter.setCurrentPrinter(bodyPrinter);
	};
}