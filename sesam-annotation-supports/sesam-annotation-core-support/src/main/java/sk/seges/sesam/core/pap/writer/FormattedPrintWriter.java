package sk.seges.sesam.core.pap.writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.builder.NameTypeUtils;
import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.writer.api.DelayedPrintWriter;

public class FormattedPrintWriter extends PrintWriter implements DelayedPrintWriter {
	
	private static final String DEFAULT_OUDENT = "\t";
	
	private int oudentLevel = 0;
	private boolean startLine = true;
	
	private boolean autoIndent = false;
	private final NameTypeUtils nameTypesUtils;
	
	public FormattedPrintWriter(Writer out, ProcessingEnvironment processingEnv) {
		super(out);
		this.nameTypesUtils = new NameTypeUtils(processingEnv);
	}

	public FormattedPrintWriter(Writer out, boolean autoFlush, ProcessingEnvironment processingEnv) {
		super(out, autoFlush);
		this.nameTypesUtils = new NameTypeUtils(processingEnv);
	}

	public FormattedPrintWriter(OutputStream out, ProcessingEnvironment processingEnv) {
		super(out);
		this.nameTypesUtils = new NameTypeUtils(processingEnv);
	}

	public FormattedPrintWriter(OutputStream out, boolean autoFlush, ProcessingEnvironment processingEnv) {
		super(out, autoFlush);
		this.nameTypesUtils = new NameTypeUtils(processingEnv);
	}

	public FormattedPrintWriter(String fileName, ProcessingEnvironment processingEnv) throws FileNotFoundException {
		super(fileName);
		this.nameTypesUtils = new NameTypeUtils(processingEnv);
	}

	public FormattedPrintWriter(String fileName, String csn, ProcessingEnvironment processingEnv) throws FileNotFoundException, UnsupportedEncodingException {
		super(fileName, csn);
		this.nameTypesUtils = new NameTypeUtils(processingEnv);
	}

	public FormattedPrintWriter(File file, ProcessingEnvironment processingEnv) throws FileNotFoundException {
		super(file);
		this.nameTypesUtils = new NameTypeUtils(processingEnv);
	}

	public FormattedPrintWriter(File file, String csn, ProcessingEnvironment processingEnv) throws FileNotFoundException, UnsupportedEncodingException {
		super(file, csn);
		this.nameTypesUtils = new NameTypeUtils(processingEnv);
	}
	
	public void setDefaultIdentLevel(int level) {
		this.oudentLevel = level;
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

	private void newLine() {
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
	
	@Override
	public void println() {
		newLine();
	}

	private ClassSerializer serializer = ClassSerializer.CANONICAL;
	private boolean typed = false;
	
	@Override
	public void setSerializer(ClassSerializer serializer) {
		this.serializer = serializer;
	}

	public void serializeTypeParameters(boolean typed) {
		this.typed = typed;
	}

	@Override
	public void println(Object x) {
		println(new Object[] {x});
	}

	@Override
	public void println(Object... x) {
		print(x);
		newLine();
	}

	private List<NamedType> usedTypes = new ArrayList<NamedType>();
	
	@Override
	public void print(Object obj) {
		print(new Object[] {obj});
	}
	
	@Override
	public void print(Object... x) {
		for (Object o: x) {
			if (o instanceof TypeMirror) {
				NamedType type = nameTypesUtils.toType((TypeMirror)o);
				if (serializer.equals(ClassSerializer.SIMPLE)) {
					usedTypes.add(type);
				}
				write(type.toString(serializer, typed));
			} else if (o instanceof Element) {
				NamedType element = nameTypesUtils.toType((Element)o);
				if (serializer.equals(ClassSerializer.SIMPLE)) {
					usedTypes.add(element);
				}
				write(element.toString(serializer, typed));
			} else if (o instanceof NamedType) {
				NamedType type = (NamedType)o;
				if (serializer.equals(ClassSerializer.SIMPLE)) {
					usedTypes.add(type);
				}
				write(type.toString(serializer, typed));
			} else if (o instanceof Class) {
				NamedType clazzType = nameTypesUtils.toType((Class<?>)o);
				if (serializer.equals(ClassSerializer.SIMPLE)) {
					usedTypes.add(clazzType);
				}
				write(clazzType.toString(serializer, typed));
			} else {
				super.write(String.valueOf(o));
			}
		}
	}
	
	public List<NamedType> getUsedTypes() {
		return usedTypes;
	}
}