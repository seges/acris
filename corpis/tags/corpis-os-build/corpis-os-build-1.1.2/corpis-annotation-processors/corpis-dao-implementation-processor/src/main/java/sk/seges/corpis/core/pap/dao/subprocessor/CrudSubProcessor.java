package sk.seges.corpis.core.pap.dao.subprocessor;

import java.io.PrintWriter;

import javax.lang.model.element.TypeElement;

import sk.seges.corpis.core.pap.dao.subprocessor.common.AbstractSubProcessor;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.dao.ICrudDAO;

public class CrudSubProcessor extends AbstractSubProcessor<ICrudDAO<?>>  {

	@Override
	public boolean process(PrintWriter printWriter, NamedType outputName, TypeElement element, TypeElement subElement) {
		printWriter.println("public " + outputName.getSimpleName() + "() {");
		printWriter.println("super(" + element.getSimpleName().toString() + ".class);");
		printWriter.println("}");
		return true;
	}
}