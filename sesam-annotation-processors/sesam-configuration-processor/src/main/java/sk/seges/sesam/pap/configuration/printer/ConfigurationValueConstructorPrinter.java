package sk.seges.sesam.pap.configuration.printer;

import java.io.PrintWriter;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementKindVisitor6;
import javax.lang.model.util.SimpleTypeVisitor6;

import sk.seges.sesam.core.configuration.api.ConfigurationValue;
import sk.seges.sesam.core.configuration.utils.ConfigurationUtils;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.configuration.model.ProcessorContext;
import sk.seges.sesam.pap.configuration.printer.api.ElementPrinter;

public class ConfigurationValueConstructorPrinter implements ElementPrinter {

	enum Te {
		
	}
	
	private FormattedPrintWriter pw;
	
	public ConfigurationValueConstructorPrinter(FormattedPrintWriter pw) {
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
			public Void visitDeclared(DeclaredType t, ProcessorContext context) {
				Boolean result = t.asElement().accept(new ElementKindVisitor6<Boolean, ProcessorContext>() {
					@Override
					public Boolean visitTypeAsEnum(TypeElement e, ProcessorContext context) {
						for (Te te: Te.values()) {
							if (te.toString().equals("")) {
								
							}
						}
						pw.println(String.class.getSimpleName() + " _" + context.getFieldName() + " = " +
								ConfigurationUtils.class.getSimpleName() + ".getConfigurationValue(configurations, \"" + context.getParameter().name() + "\");");
						pw.println("for (", e, " _enumValue: ", e, ".values()) {");
						pw.println("if (_enumValue.toString().equals(_" + context.getFieldName() + ")) {");
						pw.println("this." + context.getFieldName() + " = _enumValue;");
						pw.println("}");
						pw.println("}");
						return true;
					}
				}, context);
				
				if (result != null && result) {
					return null;
				}
				
				return super.visitDeclared(t, context);
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