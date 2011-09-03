package sk.seges.sesam.core.pap.api;

import java.io.PrintWriter;
import java.lang.reflect.Type;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.api.NamedType;

public interface SubProcessor<T> {

	Type[] getImports(TypeElement typeElement);
	
	void init(ProcessingEnvironment processingEnv);

	boolean process(PrintWriter printWriter, NamedType outputName, TypeElement element, TypeElement subElement);
}