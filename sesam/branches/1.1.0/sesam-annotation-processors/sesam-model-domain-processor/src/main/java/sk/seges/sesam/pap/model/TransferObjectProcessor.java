package sk.seges.sesam.pap.model;

import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.core.pap.configuration.api.OutputDefinition;
import sk.seges.sesam.core.pap.model.api.ArrayNamedType;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.structure.DefaultPackageValidatorProvider;
import sk.seges.sesam.core.pap.utils.ListUtils;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.pap.model.model.AbstractElementPrinter;
import sk.seges.sesam.pap.model.model.ElementPrinter;
import sk.seges.sesam.pap.model.model.ProcessorContext;
import sk.seges.sesam.pap.model.utils.TransferObjectConfiguration;
import sk.seges.sesam.pap.model.utils.TransferObjectHelper;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class TransferObjectProcessor extends AbstractTransferProcessor {
	
	class EqualsPrinter extends AbstractElementPrinter {

		public EqualsPrinter(PrintWriter pw) {
			super(pw);
		}

		@Override
		public void initialize(TypeElement typeElement) {
			pw.println("@Override");
			pw.println("public boolean equals(Object obj) {");
			pw.println("if (this == obj)");
			pw.println("	return true;");
			pw.println("if (obj == null)");
			pw.println("	return false;");
			pw.println("if (getClass() != obj.getClass())");
			pw.println("	return false;");
			
			NamedType targetClassName = getTargetClassNames(nameTypesUtils.toImmutableType(typeElement))[0];
			
			pw.println(targetClassName.toString(null, ClassSerializer.SIMPLE, true) + " other = (" + 
					targetClassName.toString(null, ClassSerializer.SIMPLE, true) + ") obj;");
		}
	
		@Override
		public void finish(TypeElement typeElement) {
			pw.println("return true;");
			pw.println("}");
		}
		
		@Override
		public void print(ProcessorContext context) {

			boolean idMethod = toHelper.isIdMethod(context.getMethod());
			
			if (idMethod) {
				//TODO That's not really true
				//id's are not interesting
				return;
			}
			
			if (context.getFieldType() instanceof ArrayNamedType) {
				pw.println("if (!" + Arrays.class.getCanonicalName() + ".equals(" + context.getFieldName() + ", other." + context.getFieldName() + "))");
				pw.println("	return false");
				return;
			}

			if (context.getFieldType().asType() == null) {
				if (idMethod) {
					pw.println("if (" + context.getFieldName() + " != null && other." + context.getFieldName() + " != null && " + context.getFieldName() + ".equals(other." + context.getFieldName() + "))");
					pw.println("	return true;");
				} else {
					pw.println("if (" + context.getFieldName() + " == null) {");
					pw.println("if (other." + context.getFieldName() + " != null)");
					pw.println("	return false;");
					pw.println("} else if (!" + context.getFieldName() + ".equals(other." + context.getFieldName() + "))");
					pw.println("	return false;");
				}
				return;
			}
			
			switch (context.getFieldType().asType().getKind()) {
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case SHORT:
				if (idMethod) {
					pw.println("if (" + context.getFieldName() + " == other." + context.getFieldName() + ")");
					pw.println("	return true;");
				} else {
					pw.println("if (" + context.getFieldName() + " != other." + context.getFieldName() + ")");
					pw.println("	return false;");
				}
				return;
			case EXECUTABLE:
			case NONE:
			case NULL:
			case OTHER:
			case PACKAGE:
			case ERROR:
			case WILDCARD:
			case VOID:
			case TYPEVAR:
				processingEnv.getMessager().printMessage(Kind.WARNING, "[WARNING] Unsupported type " + context.getFieldName() + " (" + context.getFieldType().asType().getKind() + ") in the " + 
						context.getConfigurationElement(), context.getConfigurationElement());
				return;
			case DECLARED:
				Element element = ((DeclaredType)context.getFieldType().asType()).asElement();
				switch (element.getKind()) {
				case ENUM:
				case ENUM_CONSTANT:
					if (idMethod) {
						pw.println("if (" + context.getFieldName() + " == other." + context.getFieldName() + ")");
						pw.println("	return true;");
					} else {
						pw.println("if (" + context.getFieldName() + " != other." + context.getFieldName() + ")");
						pw.println("	return false;");
					}
					return;
				case CLASS:
				case INTERFACE:
					if (idMethod) {
						pw.println("if (" + context.getFieldName() + " != null && other." + context.getFieldName() + " != null && " + context.getFieldName() + ".equals(other." + context.getFieldName() + "))");
						pw.println("	return true;");
					} else {
						pw.println("if (" + context.getFieldName() + " == null) {");
						pw.println("if (other." + context.getFieldName() + " != null)");
						pw.println("	return false;");
						pw.println("} else if (!" + context.getFieldName() + ".equals(other." + context.getFieldName() + "))");
						pw.println("	return false;");
					}
					return;
				}
			case ARRAY:
				pw.println("if (!" + Arrays.class.getCanonicalName() + ".equals(" + context.getFieldName() + ", other." + context.getFieldName() + "))");
				pw.println("	return false");
				return;
			}
		}		
	}

	class HashCodePrinter extends AbstractElementPrinter {

		public HashCodePrinter(PrintWriter pw) {
			super(pw);
		}

		@Override
		public void initialize(TypeElement typeElement) {
			pw.println("@Override");
			pw.println("public int hashCode() {");
			pw.println("final int prime = 31;");
			pw.println("int result = 1;");
		}
	
		@Override
		public void finish(TypeElement typeElement) {
			pw.println("return result;");
			pw.println("}");
		}
		
		@Override
		public void print(ProcessorContext context) {

			if (toHelper.isIdMethod(context.getMethod())) {
				//IDs are not part of the hashCode
				return;
			}

			if (context.getFieldType() instanceof ArrayNamedType) {
				pw.println("result = prime * result + " + Arrays.class.getCanonicalName() + ".hashCode(" + context.getFieldName() + ");");
				return;
			}

			if (context.getFieldType().asType() == null) {
				pw.println("result = prime * result + ((" + context.getFieldName() + " == null) ? 0 : " + context.getFieldName() + ".hashCode());");
				return;
			}
			
			switch (context.getFieldType().asType().getKind()) {
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case SHORT:
				pw.println("result = prime * result + " + context.getFieldName() + ";");
				return;
			case EXECUTABLE:
			case NONE:
			case NULL:
			case OTHER:
			case PACKAGE:
			case ERROR:
			case WILDCARD:
			case VOID:
			case TYPEVAR:
				processingEnv.getMessager().printMessage(Kind.WARNING, "[WARNING] Unsupported type " + context.getFieldName() + " (" + context.getFieldType().asType().getKind() + ") in the " + 
						context.getConfigurationElement(), context.getConfigurationElement());
				return;
			case DECLARED:
				Element element = ((DeclaredType)context.getFieldType().asType()).asElement();
				switch (element.getKind()) {
				case ENUM:
				case ENUM_CONSTANT:
				case CLASS:
				case INTERFACE:
					pw.println("result = prime * result + ((" + context.getFieldName() + " == null) ? 0 : " + context.getFieldName() + ".hashCode());");
					return;
				}
			case ARRAY:
				pw.println("result = prime * result + " + Arrays.class.getCanonicalName() + ".hashCode(" + context.getFieldName() + ");");
				return;
			}
		}		
	}

	class FieldPrinter extends AbstractElementPrinter {

		public FieldPrinter(PrintWriter pw) {
			super(pw);
		}
		
		public void print(ProcessorContext context) {
			//we do not use modifier from the param - fields should be always private
			pw.println(Modifier.PRIVATE.toString() + " " + context.getFieldType().toString(null, ClassSerializer.CANONICAL, true) + " " + 
					context.getFieldName() + ";");
			pw.println();
		}
	}
	
	class AccessorsPrinter extends AbstractElementPrinter {

		public AccessorsPrinter(PrintWriter pw) {
			super(pw);
		}
		
		@Override
		public void print(ProcessorContext context) {

			String fieldTypeName = context.getFieldType().toString(null, ClassSerializer.CANONICAL, true);
			
			String modifier = Modifier.PUBLIC.toString() + " ";
			
			//modifier = context.getModifier() != null ? (context.getModifier().toString() + " ") : "";
			
			pw.println(modifier + fieldTypeName + " " + toHelper.toGetter(context.getFieldName()) + " {");
			pw.println("return " + context.getFieldName() + ";");
			pw.println("}");
			pw.println();

			pw.println(modifier + "void " + toHelper.toSetter(context.getFieldName()) + 
					"(" + fieldTypeName + " " + context.getFieldName() + ") {");
			pw.println("this." + context.getFieldName() + " = " + context.getFieldName() + ";");
			pw.println("}");
			pw.println();
		}
	}
	
	@Override
	protected void writeClassAnnotations(Element configurationElement, NamedType outputName, PrintWriter pw) {
		pw.println("@SuppressWarnings(\"serial\")");
		
		TransferObjectConfiguration transferObjectConfiguration = new TransferObjectConfiguration(configurationElement, processingEnv);
		
		ImmutableType configurationType = (ImmutableType)getNameTypes().toType(configurationElement);
		
		pw.print("@" + TransferObjectMapping.class.getSimpleName() + "(");

		pw.println("dtoClass = " + getOutputClass(configurationType).getSimpleName() + ".class,");
		pw.println("		domainClassName = \"" + toHelper.getDomainTypeElement(configurationElement).getQualifiedName().toString() + "\", ");
		pw.println("		configurationClassName = \"" + configurationElement.toString() + "\", ");
		pw.print("		converterClassName = \"");
		if (transferObjectConfiguration.getConverter() != null) {
			pw.print(transferObjectConfiguration.getConverter().toString());
		} else {
			ImmutableType generatedConverter = TransferObjectConvertorProcessor.getOutputClass(configurationType, new DefaultPackageValidatorProvider(), processingEnv);
			pw.print(generatedConverter.getCanonicalName());
		}
		pw.print("\"");
		pw.println(")");
		
		super.writeClassAnnotations(configurationElement, outputName, pw);
	}
	
	@Override
	protected Type[] getImports(TypeElement typeElement) {
		NamedType dtoSuperclass = toHelper.getDtoSuperclass(typeElement);
		List<Type> result = new ArrayList<Type>();
		ListUtils.add(result, super.getImports(typeElement));
		if (dtoSuperclass != null) {
			ListUtils.add(result, new Type[] {dtoSuperclass});
		}
		ListUtils.add(result, new Type[] {TransferObjectMapping.class});
		return result.toArray(new Type[] {});
	}

	@Override
	protected boolean processElement(Element element, RoundEnvironment roundEnv) {

		TransferObjectConfiguration transferObjectConfiguration = new TransferObjectConfiguration(element, processingEnv);
		
		TypeElement dto = transferObjectConfiguration.getDto();
		if (dto != null) {
			return supportProcessorChain();
		}
		
		return super.processElement(element, roundEnv);
	}

	@Override
	protected Type[] getOutputDefinition(OutputDefinition type, TypeElement typeElement) {
		switch (type) {
			case OUTPUT_SUPERCLASS:
				NamedType dtoSuperclass = toHelper.getDtoSuperclass(typeElement);
				if (dtoSuperclass != null) {
					
					TypeElement domainObjectClass = toHelper.getDomainTypeElement(typeElement);
	
					if (domainObjectClass != null) {
						TypeMirror superClassType = domainObjectClass.getSuperclass();
						return new Type[] { genericsSupport.applyGenerics(dtoSuperclass, (DeclaredType)superClassType) };
					}
					
					return new Type[] {dtoSuperclass};
				}
				break;
			case OUTPUT_INTERFACES:
				List<? extends TypeMirror> interfaces = typeElement.getInterfaces();
				
				List<Type> interfaceTypes = new ArrayList<Type>();
				
				if (interfaces != null) {
					for (TypeMirror interfaceType: interfaces) {
						interfaceTypes.add(nameTypesUtils.toType(interfaceType));
					}
				}
				
				dtoSuperclass = toHelper.getDtoSuperclass(typeElement);
				if (dtoSuperclass == null) {
					ListUtils.add(interfaceTypes, nameTypesUtils.toType(Serializable.class) );
				}
				
				return interfaceTypes.toArray(new Type[] {});
		}
		
		return super.getOutputDefinition(type, typeElement);
	}
	
	@Override
	protected NamedType[] getTargetClassNames(ImmutableType mutableType) {
		return new NamedType[] {
				getOutputClass((ImmutableType)genericsSupport.applyVariableGenerics(mutableType, toHelper.getDomainTypeElement(processingEnv.getElementUtils().getTypeElement(mutableType.getCanonicalName()))))
		};
	}

	protected ElementPrinter[] getElementPrinters(PrintWriter pw) {
		return new ElementPrinter[] {
				new FieldPrinter(pw),
				new AccessorsPrinter(pw),
				new EqualsPrinter(pw),
				new HashCodePrinter(pw)
		};
	}

	public static ImmutableType getOutputClass(ImmutableType mutableType) {	
		return TransferObjectHelper.getDtoType(mutableType);
	}

	@Override
	protected boolean shouldHaveIdMethod(TypeElement configurationElement, TypeElement domainElement) {
		return false;
	}	
}