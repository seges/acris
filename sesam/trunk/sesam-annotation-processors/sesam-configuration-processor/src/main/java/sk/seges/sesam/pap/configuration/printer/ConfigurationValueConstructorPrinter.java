package sk.seges.sesam.pap.configuration.printer;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementKindVisitor6;
import javax.lang.model.util.SimpleTypeVisitor6;

import sk.seges.sesam.core.configuration.api.ConfigurationValue;
import sk.seges.sesam.core.configuration.utils.ConfigurationUtils;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.configuration.model.setting.SettingsContext;

public class ConfigurationValueConstructorPrinter extends AbstractSettingsElementPrinter {

	private FormattedPrintWriter pw;
	
	public ConfigurationValueConstructorPrinter(FormattedPrintWriter pw, MutableProcessingEnvironment processingEnv) {
		super(processingEnv);
		this.pw = pw;
	}

	@Override
	public void initialize(TypeElement type, MutableDeclaredType outputName) {
		pw.println("public " + outputName.getSimpleName() + "(", ConfigurationValue.class, "[] configurations) {");
	}

	@Override
	public void print(SettingsContext context) {
		if (context.getNestedElement() != null) {
			pw.println("this." + context.getFieldName() + " = new ", context.getNestedMutableType(), "(configurations);");
		} else {
			context.getMethod().getReturnType().accept(new SimpleTypeVisitor6<Void, SettingsContext>() {
	
				@Override
				protected Void defaultAction(TypeMirror e, SettingsContext context) {
					pw.println("this." + context.getFieldName() + " = ", ConfigurationUtils.class, ".getConfigurationValue(configurations, \"" + context.getParameterName() + "\");");
					return null;
				}
				
				@Override
				public Void visitDeclared(DeclaredType t, SettingsContext context) {
					Boolean result = t.asElement().accept(new ElementKindVisitor6<Boolean, SettingsContext>() {
						@Override
						public Boolean visitTypeAsEnum(TypeElement e, SettingsContext context) {
							pw.println(String.class, " _" + context.getFieldName() + " = ", ConfigurationUtils.class, ".getConfigurationValue(configurations, \"" + context.getParameterName() + "\");");
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
				public Void visitPrimitive(PrimitiveType t, SettingsContext context) {
					pw.print("this." + context.getFieldName() + " = ", ConfigurationUtils.class, ".getConfiguration");
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
	
					pw.println("(configurations, \"" + context.getParameterName() + "\");");
	
					return null;
				}
			}, context);
		}
	}

	@Override
	public void finish(TypeElement type) {
		pw.println("}");
		pw.println();
	}
}