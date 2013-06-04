package sk.seges.corpis.core.pap.dao.subprocessor;

import java.io.PrintWriter;
import java.lang.reflect.Type;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import sk.seges.corpis.core.pap.dao.subprocessor.common.AbstractSubProcessor;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.dao.IEntityInstancer;

public class EntityInstancerSubProcessor extends AbstractSubProcessor<IEntityInstancer<?>> {

	@Override
	public Type[] getImports() {
		return new Type[] {
				NamedType.THIS
		};
	}
	
	@Override
	public boolean process(PrintWriter printWriter, NamedType outputName, TypeElement element, TypeElement subElement) {
		ExecutableElement method = ProcessorUtils.getMethodByReturnType(subElement, element, processingEnv.getTypeUtils());
		if (method == null) {
			return false;
		}
		printWriter.println("@Override");
		printWriter.println("public " + element.getSimpleName().toString() + " " + method.toString() + "{");
		printWriter.println("return new " + element.getSimpleName().toString() + "();");
		printWriter.println("}");

		return true;
	}
}
