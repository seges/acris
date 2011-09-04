package sk.seges.sesam.pap.configuration;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.SimpleTypeVisitor6;

import sk.seges.sesam.core.configuration.annotation.Parameter;
import sk.seges.sesam.core.configuration.api.ConfigurationValue;
import sk.seges.sesam.core.configuration.utils.ConfigurationUtils;
import sk.seges.sesam.core.pap.AbstractConfigurableProcessor;
import sk.seges.sesam.core.pap.builder.NameTypesUtils;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.pap.configuration.configuration.ConfigurationProcessorConfigurer;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ConfigurationProcessor extends AbstractConfigurableProcessor {

	public static final String SUFFIX = "Holder";

	private MethodHelper methodHelper;
	
	class ProcessorContext {
		
		private TypeElement configurationElement;
		private ExecutableElement method;
		private String fieldName;
		private Parameter parameter;
		
		public void setConfigurationElement(TypeElement configurationElement) {
			this.configurationElement = configurationElement;
		}
		
		public TypeElement getConfigurationElement() {
			return configurationElement;
		}
		
		public void setMethod(ExecutableElement method) {
			this.method = method;
		}
		
		public ExecutableElement getMethod() {
			return method;
		}
		
		public String getFieldName() {
			return fieldName;
		}
		
		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}
		
		public Parameter getParameter() {
			return parameter;
		}
		
		public void setParameter(Parameter parameter) {
			this.parameter = parameter;
		}
	}
	
	interface ElementPrinter {
		void initialize(TypeElement type, NamedType outputName);
		void print(ProcessorContext context);
		void finish(TypeElement type);
	}
	
	class FieldPrinter implements ElementPrinter {

		private PrintWriter pw;
		
		public FieldPrinter(PrintWriter pw) {
			this.pw = pw;
		}
		
		@Override
		public void initialize(TypeElement type, NamedType outputName) {}

		@Override
		public void print(ProcessorContext context) {
			pw.println("private " + context.getMethod().getReturnType().toString() + " " + context.getMethod().getSimpleName().toString() + ";");
			pw.println();
		}

		@Override
		public void finish(TypeElement type) {}
	}

	class AccessorPrinter implements ElementPrinter {
		private PrintWriter pw;
		
		public AccessorPrinter(PrintWriter pw) {
			this.pw = pw;
		}
		
		@Override
		public void initialize(TypeElement type, NamedType outputName) {}

		@Override
		public void print(ProcessorContext context) {
			String fieldName = context.getFieldName();
			pw.println("public " + context.getMethod().getReturnType().toString() + " " + methodHelper.toGetter(fieldName) + "() {");
			pw.println("return " + fieldName + ";");
			pw.println("}");
			pw.println("");
			pw.println("public void " + methodHelper.toSetter(fieldName) + "(" + context.getMethod().getReturnType().toString() + " " + fieldName + ") {");
			pw.println("return this." + fieldName + " = " + fieldName + ";");
			pw.println("}");
			pw.println("");
		}

		@Override
		public void finish(TypeElement type) {}
	}

	class ConfigurationValueConstructorPrinter implements ElementPrinter {

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
							ConfigurationUtils.class.getSimpleName() + ".getConfigurationValue(configurations, " + context.getParameter().name() + ");");
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

					pw.println("(configurations, " + context.getParameter().name() + ");");

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

	class CopyConstructorDefinitionPrinter implements ElementPrinter {

		private PrintWriter pw;
		private String instanceName;
		
		public CopyConstructorDefinitionPrinter(PrintWriter pw) {
			this.pw = pw;
		}

		@Override
		public void initialize(TypeElement type, NamedType outputName) {
			instanceName = methodHelper.toField(outputName.getSimpleName());
			pw.println("public " + outputName.getSimpleName() + "(" + outputName.getSimpleName() + " " + instanceName + ") {");
		}

		@Override
		public void print(ProcessorContext context) {
			String name = context.getMethod().getSimpleName().toString();
			pw.print("this." + name + " = " + instanceName + "." + methodHelper.toGetter(context.getFieldName() + ";"));
		}

		@Override
		public void finish(TypeElement type) {
			pw.println("}");
			pw.println();
		}
	}

	class EnumeratedConstructorDefinitionPrinter implements ElementPrinter {

		private PrintWriter pw;
		private int index = 0;
		
		public EnumeratedConstructorDefinitionPrinter(PrintWriter pw) {
			this.pw = pw;
		}

		@Override
		public void initialize(TypeElement type, NamedType outputName) {
			pw.println("public " + outputName.getSimpleName() + "(");
		}

		@Override
		public void print(ProcessorContext context) {
			if (index > 0) {
				pw.print(", ");
			}
			pw.print(context.getMethod().getReturnType() + " " + context.getMethod().getSimpleName().toString());
			index++;
		}

		@Override
		public void finish(TypeElement type) {
			pw.println(") {");
		}
	}

	class EnumeratedConstructorBodyPrinter implements ElementPrinter {

		private PrintWriter pw;
		
		public EnumeratedConstructorBodyPrinter(PrintWriter pw) {
			this.pw = pw;
		}

		@Override
		public void initialize(TypeElement type, NamedType outputName) {
		}

		@Override
		public void print(ProcessorContext context) {
			String name = context.getMethod().getSimpleName().toString();
			pw.println("this." + name + " = " + name + ";");
		}

		@Override
		public void finish(TypeElement type) {
			pw.println("}");
			pw.println();
		}
	}

	class MergePrinter implements ElementPrinter {

		private PrintWriter pw;
		private String instanceName;
		
		public MergePrinter(PrintWriter pw) {
			this.pw = pw;
		}
		
		@Override
		public void initialize(TypeElement type, NamedType outputName) {
			instanceName = methodHelper.toField(outputName.getSimpleName());
			pw.println("public " + outputName.getSimpleName() + " merge(" + outputName.getSimpleName() + " " + instanceName + ") {");
			pw.println("if (" + instanceName + " == null) {");
			pw.println("return this;");
			pw.println("}");
		}

		@Override
		public void print(ProcessorContext context) {
			pw.println("if (" + context.getFieldName() + " == null) {");
			pw.println("this." + context.getFieldName() + " = " + instanceName + "." + methodHelper.toGetter(context.getFieldName()) + "();");
			pw.println("}");
		}

		@Override
		public void finish(TypeElement type) {
			pw.println("}");
		}
		
	}
	
	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new ConfigurationProcessorConfigurer();
	}
	
	@Override
	protected Type[] getImports() {
		return new Type[] {
				ConfigurationValue.class
		};
	}
	
	@Override
	protected NamedType[] getTargetClassNames(ImmutableType mutableType) {
		return new NamedType[] { mutableType.addClassSufix(SUFFIX) };
	}

	protected ElementPrinter[] getElementPrinters(PrintWriter pw) {
		return new ElementPrinter[] {
				new FieldPrinter(pw),
				new AccessorPrinter(pw),
				new ConfigurationValueConstructorPrinter(pw),
				new EnumeratedConstructorDefinitionPrinter(pw),
				new EnumeratedConstructorBodyPrinter(pw),
				new CopyConstructorDefinitionPrinter(pw)
		};
	}

	@Override
	public synchronized void init(ProcessingEnvironment pe) {
		super.init(pe);
		methodHelper = new MethodHelper(pe, new NameTypesUtils(pe));
	}
	
	private boolean initializeContext(ProcessorContext context) {
		String fieldName = context.getMethod().getSimpleName().toString();
		context.setFieldName(fieldName);
		
		return true;
	}
	
	@Override
	protected void processElement(TypeElement typeElement, NamedType outputName, RoundEnvironment roundEnv, PrintWriter pw) {
		List<ExecutableElement> methods = ElementFilter.methodsIn(typeElement.getEnclosedElements());

		for (ElementPrinter printer: getElementPrinters(pw)) {
			printer.initialize(typeElement, outputName);
			for (ExecutableElement method: methods) {
				Parameter parameterAnnotation = method.getAnnotation(Parameter.class);
				
				if (parameterAnnotation != null && !method.getReturnType().getKind().equals(TypeKind.VOID)) {
					ProcessorContext context = new ProcessorContext();
					context.setMethod(method);
					context.setConfigurationElement(typeElement);
					context.setParameter(parameterAnnotation);
					if (initializeContext(context)) {
						printer.print(context);
					} else {
						//TODO print error message
					}
				}
			}
			printer.finish(typeElement);
		}
	}
}