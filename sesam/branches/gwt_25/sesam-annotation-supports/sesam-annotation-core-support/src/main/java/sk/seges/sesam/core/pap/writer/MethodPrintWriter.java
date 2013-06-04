package sk.seges.sesam.core.pap.writer;

import java.util.List;

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
		//HierarchyPrintWriter methodPrintWriter = method.getPrintWriter();

//		HierarchyPrintWriter methodPrinter = new HierarchyPrintWriter(processingEnv);

//		methodPrintWriter.addNestedPrinter(methodPrinter);
		
		addNestedPrinter(new HierarchyPrintWriter(processingEnv) {
			@Override
			public void flush() {
				new AnnotationPrinter(this, processingEnv).printMethodAnnotations(method);
				super.flush();
			}
		});

		addNestedPrinter(new HierarchyPrintWriter(processingEnv) {
			@Override
			public void flush() {
				new MethodPrinter(this, processingEnv).printMethodDefinition(method);
				super.flush();
			}
		});

		println(" {");
		
		HierarchyPrintWriter bodyPrinter = new HierarchyPrintWriter(processingEnv);
		addNestedPrinter(bodyPrinter);

		println("}");
		
		setCurrentPrinter(bodyPrinter);
	};
	
	private void handleNestedPrinters(List<FormattedPrintWriter> nestedPrinters) {
		for (FormattedPrintWriter nestedPrinter: nestedPrinters) {
			if (nestedPrinter instanceof LazyPrintWriter) {
				((LazyPrintWriter)nestedPrinter).printManually();
			} else if (nestedPrinter instanceof HierarchyPrintWriter) {
				handleNestedPrinters(((HierarchyPrintWriter)nestedPrinter).getNestedPrinters());
			}
		}
	}
	
	@Override
	public void flush() {
		
		handleNestedPrinters(method.getPrintWriter().getNestedPrinters());
		
		super.flush();
	}
}