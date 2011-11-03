package sk.seges.sesam.core.pap.test.manager;

import java.io.File;
import java.util.Collection;
import java.util.List;

import javax.annotation.processing.Processor;

public abstract class AbstractCompilerManager implements CompilerManager{

	protected List<String> compilerOptions;
	protected Collection<Processor> processors;

	public AbstractCompilerManager() {}

	protected void printCompilerOptions(Collection<File> compilationUnits) {
		for (String option : compilerOptions) {
			System.out.print(option + " ");
		}
		System.out.print("-processor ");
		for (Processor processor : this.processors) {
			System.out.print(processor.getClass().getCanonicalName() + " ");
		}
		System.out.print(" ");
		for (File file : compilationUnits) {
			System.out.print(file.getName() + " ");
		}
		System.out.println();
		System.out.println();
	}
	
	@Override
	public void setOptions(List<String> compilerOptions) {
		this.compilerOptions = compilerOptions;
	}

	@Override
	public void setProcessors(Collection<Processor> processors) {
		this.processors = processors;
	}
}