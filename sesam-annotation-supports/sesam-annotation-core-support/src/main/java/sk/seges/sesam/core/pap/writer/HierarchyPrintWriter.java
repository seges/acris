package sk.seges.sesam.core.pap.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import sk.seges.sesam.core.pap.api.annotation.support.PrintSupport;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.printer.ImportPrinter;

public class HierarchyPrintWriter extends FormattedPrintWriter {

	private final String lineSeparator;

	private List<FormattedPrintWriter> nestedPrinters = new ArrayList<FormattedPrintWriter>();
	private HierarchyPrintWriter currentPrinter;
	private final PrintSupport printerSupport;

	public HierarchyPrintWriter(PrintSupport printerSupport, MutableProcessingEnvironment processingEnv, List<MutableDeclaredType> usedTypes) {
		this(printerSupport, processingEnv, new ByteArrayOutputStream(), usedTypes);
	}
	
	public HierarchyPrintWriter(PrintSupport printerSupport, MutableProcessingEnvironment processingEnv, OutputStream os, List<MutableDeclaredType> usedTypes) {
		super(os, printerSupport, processingEnv, usedTypes);

		this.printerSupport = printerSupport;
		this.lineSeparator = java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"));
	}

	protected void initializePrinter() {
		this.nestedPrinters.add(new FormattedPrintWriter(printerSupport, processingEnv, getUsedTypes()));
		setAutoIndent(autoIndent);
		setDefaultIdentLevel(oudentLevel);
	}
	
	public PrintSupport getPrinterSupport() {
		return printerSupport;
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
			} else {
				printWriter.flush();
				String string = null;
				
	//			if (i == nestedPrinters.size()) {
	//				string = toString(printWriter.getOutputStream());
	//			} else {
				
					string = printWriter.getOutputStream().toString();
	//			}
	
					nestedOutputs.add(string);
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
				getOutputStream().write(string.getBytes());
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage());
			}
			
			i++;
		}
		
		super.flush();
	}

	private String toString(OutputStream outputStream) {
		String s = outputStream.toString();
		if (s.endsWith(lineSeparator)) {
			s = s.substring(0, s.length() - lineSeparator.length());
		}
		int i = 0;
		while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
			i++;
		}
		return s.substring(i);
	}

	public <T extends FormattedPrintWriter> T addNestedPrinter(T pw) {
		if (currentPrinter != null) {
			return currentPrinter.addNestedPrinter(pw);
		}
		
		int oudentLevel = getOudentLevel();
		nestedPrinters.add(pw);
		pw.setDefaultIdentLevel(oudentLevel);
		nestedPrinters.add(new FormattedPrintWriter(printerSupport, processingEnv, getUsedTypes()));
		getCurrentPrinter().setDefaultIdentLevel(oudentLevel);
		return pw;
	}
	
	public HierarchyPrintWriter initializeNestedPrinter() {
		if (currentPrinter != null) {
			return currentPrinter.initializeNestedPrinter();
		}
		
		return addNestedPrinter(new HierarchyPrintWriter(processingEnv.getPrintSupport(), processingEnv, getUsedTypes()));
	}

	@Override
	protected void setOudentLevel(int oudentLevel) {
		getCurrentPrinter().setOudentLevel(oudentLevel);
	}
	
	@Override
	public int getOudentLevel() {
		return getCurrentPrinter().getOudentLevel();
	}
	
	@Override
	public List<MutableDeclaredType> getUsedTypes() {
		return usedTypes;
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
	public void setDefaultIdentLevel(int level) {
		for (FormattedPrintWriter pw: nestedPrinters) {
			pw.setDefaultIdentLevel(level);
		}
		super.setDefaultIdentLevel(level);
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