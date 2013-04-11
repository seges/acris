package sk.seges.sesam.core.pap.writer;

import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;

public abstract class LazyPrintWriter extends FormattedPrintWriter {

	public LazyPrintWriter(MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
	}

	protected abstract void print();
	
	private boolean printed = false;
	
	protected void printManually() {
		if (!printed) {
			print();
			printed = true;
		}
	}
	
	@Override
	public void flush() {
		printManually();
		super.flush();
	}
}
