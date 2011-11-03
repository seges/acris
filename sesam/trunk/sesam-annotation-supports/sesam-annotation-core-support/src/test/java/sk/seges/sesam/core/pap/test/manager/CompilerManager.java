package sk.seges.sesam.core.pap.test.manager;

import java.util.Collection;
import java.util.List;

import javax.annotation.processing.Processor;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

public interface CompilerManager {

	void setOptions(List<String> compilerOptions);
	void setProcessors(Collection<Processor> processors);
	
	List<Diagnostic<? extends JavaFileObject>> run();
}
