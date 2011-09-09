package sk.seges.sesam.pap.configuration.printer;

import java.io.PrintWriter;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleTypeVisitor6;

import sk.seges.sesam.core.configuration.api.ConfigurationValue;
import sk.seges.sesam.core.configuration.utils.ConfigurationUtils;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.pap.configuration.model.ProcessorContext;
import sk.seges.sesam.pap.configuration.printer.api.ElementPrinter;

public class ConfigurationValueConstructorPrinter implements ElementPrinter {

	private PrintWriter pw;
	
	public ConfigurationValueConstructorPrinter(PrintWriter pw) {
		this.pw = pw;
	}

	@Override
	public void initialize(TypeElement type, NamedType outputName) {
		pw.println("public " + outputName.getSimpleName() + "(" + ConfigurationValue.class.getSimpleName() + "[] configurations) {");
	}

	@Override
	public void print(ProcessorContext context) {
		context.getMethod().getReturnType().accept(new SimpleTypeVisitor6<Void, ProcessorContext>() {

			@Override
			protected Void defaultAction(TypeMirror e, ProcessorContext context) {
				pw.println("this." + context.getFieldName() + " = " +
						ConfigurationUtils.class.getSimpleName() + ".getConfigurationValue(configurations, \"" + context.getParameter().name() + "\");");
				return null;
			}
			
			@Override
			public Void visitPrimitive(PrimitiveType t, ProcessorContext context) {
				pw.print("this." + context.getFieldName() + " = " +
						ConfigurationUtils.class.getSimpleName() + ".getConfiguration");
				switch (t.getKind()) {
				case BOOLEAN:
					pw.print("Boolean");
					break;
				case BYTE:
					pw.print("Byte");
					break;
				case CHAR:
					pw.print("Char");
					break;
				case DOUBLE:
					pw.print("Double");
					break;
				case FLOAT:
					pw.print("Float");
					break;
				case INT:
					pw.print("Int");
					break;
				case LONG:
					pw.print("Long");
					break;
				case SHORT:
					pw.print("Short");
					break;
				}

				pw.println("(configurations, \"" + context.getParameter().name() + "\");");

				return null;
			}
		}, context);
	}

	@Override
	public void finish(TypeElement type) {
		pw.println("}");
		pw.println();
	}
}