package sk.seges.sesam.core.pap.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.printer.ImportPrinter;

public class HierarchyPrintWriter extends FormattedPrintWriter {

	private List<FormattedPrintWriter> nestedPrinters = new ArrayList<FormattedPrintWriter>();
	private HierarchyPrintWriter currentPrinter;

	public HierarchyPrintWriter(MutableProcessingEnvironment processingEnv) {
		this(processingEnv, new ByteArrayOutputStream());
	}
	
	public HierarchyPrintWriter(MutableProcessingEnvironment processingEnv, OutputStream os) {
		super(os, processingEnv);
	}

	protected void initializePrinter() {
		this.nestedPrinters.add(new FormattedPrintWriter(processingEnv));
		setAutoIndent(autoIndent);
		setOudentLevel(oudentLevel);
	}
	
	public void setCurrentPrinter(HierarchyPrintWriter bodyPrinter) {
		this.currentPrinter = bodyPrinter;
	}
	
	@Override
	public void flush() {

		int i = 1;

		List<String> nestedOutputs = new ArrayList<String>();
		
		for (FormattedPrintWriter printWriter: nestedPrinters) {
			if (printWriter instanceof ImportPrinter) {
				nestedOutputs.add("");
			} else if (printWriter instanceof HierarchyPrintWriter) {
				printWriter.flush();
				nestedOutputs.add(toString((HierarchyPrintWriter) printWriter));
			} else {
				printWriter.flush();
				nestedOutputs.add(printWriter.toString());
			}
		}
		
		i = 0;
		
		for (String output: nestedOutputs) {
			String string = null;
			if (nestedPrinters.get(i) instanceof ImportPrinter) {
				nestedPrinters.get(i).flush();
				string = nestedPrinters.get(i).getOutputStream().toString();
			} else {
				string = output;
			}
			try {
				if (string == null) {
					//Strange workaround for writing a new line ... without empty it does not work
					getOutputStream().write((" " + lineSeparator).getBytes());
				} else {
					getOutputStream().write(string.getBytes());
				}
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage());
			}
			
			i++;
		}
		
		super.flush();
	}

	public String toString(HierarchyPrintWriter printWriter) {
		return printWriter.toString(printWriter.getDefaultOudentLevel());
	}

	public <T extends FormattedPrintWriter> T addNestedPrinter(T pw) {
		if (currentPrinter != null) {
			return currentPrinter.addNestedPrinter(pw);
		}
		
		int oudentLevel = getOudentLevel();
		nestedPrinters.add(pw);
		pw.setDefaultOudentLevel(oudentLevel);
		nestedPrinters.add(new FormattedPrintWriter(processingEnv));
		getCurrentPrinter().setDefaultOudentLevel(oudentLevel);
		return pw;
	}
	
	public HierarchyPrintWriter initializeNestedPrinter() {
		if (currentPrinter != null) {
			return currentPrinter.initializeNestedPrinter();
		}
		
		return addNestedPrinter(new HierarchyPrintWriter(processingEnv));
	}

	@Override
	public int getOudentLevel() {
		return getCurrentPrinter().getOudentLevel();
	}
	
	@Override
	public void close() {
		for (PrintWriter pw: nestedPrinters) {
			pw.close();
		}
		super.close();
	}
	
	@Override
	public void write(String text, int off, int len) {
		getCurrentPrinter().write(text, off, len);
	}
	
	@Override
	public void println() {
		getCurrentPrinter().println();
	}

	@Override
	public int getCurrentPosition() {
		return getCurrentPrinter().getCurrentPosition();
	}
	
	@Override
	public void print(Object... x) {
		getCurrentPrinter().print(x);
	}

	private FormattedPrintWriter getCurrentPrinter() {
		if (nestedPrinters.size() == 0) {
			initializePrinter();
		}

		if (currentPrinter != null) {
			return currentPrinter;
		}
		
		return nestedPrinters.get(nestedPrinters.size() - 1);
	}

	@Override
	public void setAutoIndent(boolean autoIndent) {
		if (nestedPrinters != null) {
			for (FormattedPrintWriter pw: nestedPrinters) {
				pw.setAutoIndent(autoIndent);
			}
		}
		super.setAutoIndent(autoIndent);
	}

//	@Override
//	public void setSerializer(ClassSerializer serializer) {
//		getCurrentPrinter().setSerializer(serializer);
//	}
//
//	@Override
//	public void serializeTypeParameters(boolean typed) {
//		getCurrentPrinter().serializeTypeParameters(typed);
//	}
}