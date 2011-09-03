package sk.seges.sesam.pap.model.printer.method;

import java.io.PrintWriter;

import sk.seges.sesam.pap.model.context.api.ProcessorContext;

public interface CopyMethodPrinter {

	void printCopyMethod(ProcessorContext context, PrintWriter pw);
}