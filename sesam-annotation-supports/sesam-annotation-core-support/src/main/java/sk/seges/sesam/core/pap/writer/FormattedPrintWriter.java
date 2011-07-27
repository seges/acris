package sk.seges.sesam.core.pap.writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class FormattedPrintWriter extends PrintWriter {
	
	private static final String DEFAULT_OUDENT = "\t";
	
	private int oudentLevel = 0;
	private boolean startLine = true;
	
	private boolean autoIndent = false;

	public FormattedPrintWriter(Writer out) {
		super(out);
	}

	public FormattedPrintWriter(Writer out, boolean autoFlush) {
		super(out, autoFlush);
	}

	public FormattedPrintWriter(OutputStream out) {
		super(out);
	}

	public FormattedPrintWriter(OutputStream out, boolean autoFlush) {
		super(out, autoFlush);
	}

	public FormattedPrintWriter(String fileName) throws FileNotFoundException {
		super(fileName);
	}

	public FormattedPrintWriter(String fileName, String csn) throws FileNotFoundException, UnsupportedEncodingException {
		super(fileName, csn);
	}

	public FormattedPrintWriter(File file) throws FileNotFoundException {
		super(file);
	}

	public FormattedPrintWriter(File file, String csn) throws FileNotFoundException, UnsupportedEncodingException {
		super(file, csn);
	}
	
	public void setAutoIndent(boolean autoIndent) {
		this.autoIndent = autoIndent;
	}
	
	public void indent() {
		if (autoIndent) {
			throw new RuntimeException("Unable to indent manually in auto mode. Please set autoIndent to false.");
		}
		oudentLevel = (oudentLevel <= 0) ? 0 : oudentLevel-1;
	}
	
	public void oudent() {
		if (autoIndent) {
			throw new RuntimeException("Unable to oudent manually in auto mode. Please set autoIndent to false.");
		}
		oudentLevel++;
	}

	private void setAutoOudent(char c) {
		if (c == '{') {
			oudentLevel++;
		}
	}

	private void setAutoIndent(char c) {
		if (c == '}') {
			oudentLevel = (oudentLevel <= 0) ? 0 : oudentLevel-1;
		}
	}

	private boolean processing = false;
	
	private void addIdentation() {
		if (processing) {
			return;
		}
		processing = true;
		if (startLine) {
			String indentation = "";
			
			for (int i = 0; i < oudentLevel; i++) {
				indentation += DEFAULT_OUDENT;
			}
			
			super.print(indentation);
			startLine = false;
		}
		processing = false;
	}

	private Character lastCharacted = null;
	
	@Override
	public void write(String text, int off, int len) {
		if (text != null && text.length() > off && autoIndent) {
			setAutoIndent(text.charAt(off));
		}
		addIdentation();
		super.write(text, off, len);
		if (text != null && text.length() > off + len - 1 && len > 0 && autoIndent) {
			lastCharacted = text.charAt(off + len - 1);
		}
	}

	@Override
	public void println() {
		if (lastCharacted != null && autoIndent) {
			setAutoOudent(lastCharacted);
			lastCharacted = null;
		}
		super.println();
		startLine = true;
	}
}