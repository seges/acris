package sk.seges.corpis.core.pap.dao.subprocessor.common;

import java.lang.reflect.Type;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.api.SubProcessor;
import sk.seges.sesam.core.pap.builder.NameTypesUtils;

public abstract class AbstractSubProcessor<T> implements SubProcessor<T> {

	protected ProcessingEnvironment processingEnv;
	protected NameTypesUtils nameTypesUtils;
	
	@Override
	public Type[] getImports(TypeElement typeElement) {
		return new Type[] {
				
		};
	}

	@Override
	public void init(ProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
		this.nameTypesUtils = new NameTypesUtils(processingEnv);
	}
}