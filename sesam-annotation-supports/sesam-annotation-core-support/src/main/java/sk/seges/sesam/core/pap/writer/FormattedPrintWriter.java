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

	private String lastText = null;
	
	@Override
	public void write(String text, int off, int len) {
		if (!processing) {
			if (text != null && text.length() > off) {
				setAutoIndent(text.charAt(off));
			}
			addIdentation();
		}
		super.write(text, off, len);
		if (!processing) {
			lastText += text.substring(off, len);
		}
	}

	@Override
	public void println() {
		if (lastText != null && autoIndent) {
			for (int i = 0; i < lastText.length(); i++) {
				if (i > 0) {
					setAutoIndent(lastText.charAt(i));
				}
				setAutoOudent(lastText.charAt(i));
			}
		}
		super.println();
		startLine = true;
		lastText = "";
	}
}