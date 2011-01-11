package sk.seges.corpis.core.pap.dao.subprocessor.common;

import java.lang.reflect.Type;

import javax.annotation.processing.ProcessingEnvironment;

import sk.seges.sesam.core.pap.api.SubProcessor;

public abstract class AbstractSubProcessor<T> implements SubProcessor<T> {

	protected ProcessingEnvironment processingEnv;
	
	@Override
	public Type[] getImports() {
		return new Type[] {
				
		};
	}

	@Override
	public void init(ProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
	}
}