package sk.seges.sesam.pap.model.printer.method;

import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.ProcessorContext;

public interface CopyMethodPrinter {

	void printCopyMethod(ProcessorContext context, FormattedPrintWriter pw);
}