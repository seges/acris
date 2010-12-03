package sk.seges.acris.recorder.client.stacktrace;

public class StackTraceItem {
	private String className;

	private String fileName;

	private int lineNumber;

	private String methodName;

	public String getClassName() {
		return className;
	}

	public String getFileName() {
		return fileName;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public String getMethodName() {
		return methodName;
	}

	public StackTraceItem(final String declaringClass, final String methodName, final String fileName, final int lineNumber) {
		this.setClassName(declaringClass);
		this.setMethodName(methodName);
		this.setFileName(fileName);
		this.setLineNumber(lineNumber);
	}

	void setClassName(final String className) {
		this.className = className;
	}

	void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	void setLineNumber(final int lineNumber) {
		this.lineNumber = lineNumber;
	}

	void setMethodName(final String methodName) {
		this.methodName = methodName;
	}
}